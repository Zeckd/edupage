const API_BASE = '/api';

let token = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');

if (!token || !currentUser.role) {
    window.location.href = '/index.html';
}

function getAuthHeaders() {
    const currentToken = localStorage.getItem('token');
    if (!currentToken) {
        console.error('Token not found in localStorage');
        // Если токена нет, все равно возвращаем заголовки, но без токена
        // Это позволит серверу вернуть 401, который будет обработан перехватчиком
    }
    const headers = {
        'Content-Type': 'application/json'
    };
    if (currentToken) {
        headers['Authorization'] = `Bearer ${currentToken}`;
    }
    return headers;
}

// Перехватываем все fetch запросы для обработки ошибок авторизации
const originalFetch = window.fetch;
window.fetch = async function(...args) {
    const response = await originalFetch(...args);
    
    // Если получили 401 или 403, значит токен невалиден или нет доступа
    // НЕ делаем редирект для запросов к API, которые могут обработать ошибку сами
    if (response.status === 401 || response.status === 403) {
        const url = args[0];
        // Проверяем, это запрос к API или нет
        if (typeof url === 'string' && url.startsWith('/api/')) {
            // Для API запросов не делаем автоматический редирект
            // Пусть обработчик запроса сам решает, что делать
            console.warn('Unauthorized or Forbidden response:', url, response.status);
        } else {
            // Для не-API запросов делаем редирект
            localStorage.removeItem('token');
            localStorage.removeItem('currentUser');
            if (!window.location.pathname.includes('index.html')) {
                window.location.href = '/index.html';
            }
        }
    }
    
    return response;
};

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    window.location.href = '/index.html';
}

function showSection(sectionName) {
    document.querySelectorAll('.section').forEach(s => s.style.display = 'none');
    document.querySelectorAll('.sidebar a').forEach(a => a.classList.remove('active'));
    const section = document.getElementById(`${sectionName}-section`);
    if (section) {
        section.style.display = 'block';
    }
    if (event && event.target) {
        event.target.classList.add('active');
    }
}

function showModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// WebSocket для уведомлений
let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/api/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        
        // Подписываемся на персональные уведомления
        stompClient.subscribe(`/queue/notifications/${currentUser.userId}`, function(notification) {
            const data = JSON.parse(notification.body);
            showNotification(data.message);
            updateNotificationBadge();
        });
        
        // Подписываемся на уведомления по роли
        stompClient.subscribe(`/topic/notifications/${currentUser.role.toLowerCase()}`, function(message) {
            showNotification(message.body);
            updateNotificationBadge();
        });
    });
}

function showNotification(message) {
    // Создаем красивое уведомление
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #667eea;
        color: white;
        padding: 15px 20px;
        border-radius: 5px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 10000;
        max-width: 300px;
        animation: slideIn 0.3s ease-out;
    `;
    notification.textContent = message;
    document.body.appendChild(notification);
    
    // Автоматически скрываем через 5 секунд
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => notification.remove(), 300);
    }, 5000);
    
    // Обновляем бейдж уведомлений
    updateNotificationBadge();
}

// Добавляем анимации для уведомлений
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

async function updateNotificationBadge() {
    try {
        const response = await fetch(`${API_BASE}/notifications`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const notifications = await response.json();
            const unreadCount = notifications.filter(n => !n.readFlag).length;
            const badge = document.getElementById('notificationBadge');
            if (badge) {
                if (unreadCount > 0) {
                    badge.textContent = unreadCount;
                    badge.style.display = 'flex';
                } else {
                    badge.style.display = 'none';
                }
            }
        }
    } catch (error) {
        console.error('Error updating notifications:', error);
    }
}

let notificationsModal = null;

function showNotifications() {
    if (!notificationsModal) {
        createNotificationsModal();
    }
    loadNotifications();
    notificationsModal.style.display = 'block';
}

function createNotificationsModal() {
    notificationsModal = document.createElement('div');
    notificationsModal.className = 'modal';
    notificationsModal.id = 'notificationsModal';
    notificationsModal.innerHTML = `
        <div class="modal-content" style="max-width: 600px;">
            <span class="close" onclick="closeNotificationsModal()">&times;</span>
            <h2>Уведомления</h2>
            <div style="margin-bottom: 10px;">
                <button class="btn btn-secondary" onclick="markAllAsRead()">Отметить все как прочитанные</button>
            </div>
            <div id="notificationsList" style="max-height: 400px; overflow-y: auto;"></div>
        </div>
    `;
    document.body.appendChild(notificationsModal);
}

function closeNotificationsModal() {
    if (notificationsModal) {
        notificationsModal.style.display = 'none';
    }
}

async function loadNotifications() {
    try {
        const response = await fetch(`${API_BASE}/notifications`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const notifications = await response.json();
            const list = document.getElementById('notificationsList');
            if (notifications.length === 0) {
                list.innerHTML = '<p>Нет уведомлений</p>';
            } else {
                list.innerHTML = notifications.map(notif => `
                    <div class="notification-item" style="padding: 10px; margin-bottom: 10px; border: 1px solid #ddd; border-radius: 5px; background: ${notif.readFlag ? '#f9f9f9' : '#fff3cd'};">
                        <p style="margin: 0; font-weight: ${notif.readFlag ? 'normal' : 'bold'};">${notif.message}</p>
                        <small style="color: #666;">${new Date(notif.createdAt).toLocaleString('ru-RU')}</small>
                        ${!notif.readFlag ? `<button class="btn btn-success" style="margin-top: 5px; padding: 5px 10px;" onclick="markAsRead(${notif.id})">Отметить как прочитанное</button>` : ''}
                    </div>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error loading notifications:', error);
    }
}

async function markAsRead(notificationId) {
    try {
        const response = await fetch(`${API_BASE}/notifications/${notificationId}/read`, {
            method: 'PUT',
            headers: getAuthHeaders()
        });
        if (response.ok) {
            loadNotifications();
            updateNotificationBadge();
        }
    } catch (error) {
        console.error('Error marking as read:', error);
    }
}

async function markAllAsRead() {
    try {
        const response = await fetch(`${API_BASE}/notifications/read-all`, {
            method: 'PUT',
            headers: getAuthHeaders()
        });
        if (response.ok) {
            loadNotifications();
            updateNotificationBadge();
        }
    } catch (error) {
        console.error('Error marking all as read:', error);
    }
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    if (notificationsModal && event.target === notificationsModal) {
        closeNotificationsModal();
    }
}

// Подключаем WebSocket при загрузке страницы
if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
    connectWebSocket();
}

