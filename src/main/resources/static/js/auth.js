const API_BASE = '/api';

let token = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');

// Проверка авторизации - проверяем валидность токена перед редиректом
if (token && currentUser.role) {
    // Проверяем токен через API запрос
    fetch(`${API_BASE}/notifications`, {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            // Токен валиден - перенаправляем
            redirectToDashboard(currentUser.role);
        } else {
            // Токен невалиден - очищаем
            localStorage.removeItem('token');
            localStorage.removeItem('currentUser');
        }
    }).catch(() => {
        // Ошибка сети - очищаем токен на всякий случай
        localStorage.removeItem('token');
        localStorage.removeItem('currentUser');
    });
}

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            token = data.token;
            currentUser = {
                username: data.username,
                role: data.role,
                userId: data.userId
            };
            localStorage.setItem('token', token);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            redirectToDashboard(data.role);
        } else {
            showError('loginForm', 'Неверное имя пользователя или пароль');
        }
    } catch (error) {
        showError('loginForm', 'Ошибка подключения к серверу');
    }
});

function showError(formId, message) {
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
        setTimeout(() => {
            errorDiv.classList.remove('show');
        }, 5000);
    }
}

function redirectToDashboard(role) {
    switch(role) {
        case 'ADMIN':
            window.location.href = '/admin.html';
            break;
        case 'TEACHER':
            window.location.href = '/teacher.html';
            break;
        case 'STUDENT':
            window.location.href = '/student.html';
            break;
    }
}


