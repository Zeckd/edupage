document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('userName').textContent = currentUser.username;
    loadDashboard();
    loadUsers();
    loadStudents();
    loadTeachers();
    loadGroups();
    loadSubjects();
    loadAssignmentData();
    updateNotificationBadge();
    
    // Перезагружаем данные при переключении секций
    const originalShowSection = window.showSection;
    window.showSection = function(sectionName) {
        originalShowSection(sectionName);
        if (sectionName === 'assignments') {
            loadAssignmentData();
        } else if (sectionName === 'notifications') {
            // Данные загружаются при открытии формы
        }
    };
});

async function loadDashboard() {
    try {
        const response = await fetch(`${API_BASE}/admin/dashboard`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const data = await response.json();
            document.getElementById('statUsers').textContent = data.totalUsers;
            document.getElementById('statStudents').textContent = data.totalStudents;
            document.getElementById('statTeachers').textContent = data.totalTeachers;
            document.getElementById('statGroups').textContent = data.totalGroups;
        }
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE}/admin/users`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const users = await response.json();
            const tbody = document.getElementById('usersTableBody');
            tbody.innerHTML = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deleteUser(${user.id})">Удалить</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

async function loadStudents() {
    try {
        const response = await fetch(`${API_BASE}/admin/students`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const students = await response.json();
            const tbody = document.getElementById('studentsTableBody');
            tbody.innerHTML = students.map(student => `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.firstName || 'N/A'}</td>
                    <td>${student.lastName || 'N/A'}</td>
                    <td>${student.groupName || 'N/A'}</td>
                    <td>${student.email || 'N/A'}</td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading students:', error);
    }
}

async function loadTeachers() {
    try {
        const response = await fetch(`${API_BASE}/admin/teachers`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const teachers = await response.json();
            const tbody = document.getElementById('teachersTableBody');
            tbody.innerHTML = teachers.map(teacher => {
                return `
                <tr>
                    <td>${teacher.id}</td>
                    <td>${teacher.firstName || 'N/A'}</td>
                    <td>${teacher.lastName || 'N/A'}</td>
                    <td>${teacher.email || 'N/A'}</td>
                    <td>${teacher.hireDate || 'N/A'}</td>
                </tr>
            `;
            }).join('');
        } else {
            console.error('Failed to load teachers:', response.status);
        }
    } catch (error) {
        console.error('Error loading teachers:', error);
    }
}

async function loadGroups() {
    try {
        const response = await fetch(`${API_BASE}/admin/groups`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const groups = await response.json();
            const tbody = document.getElementById('groupsTableBody');
            tbody.innerHTML = groups.map(group => `
                <tr>
                    <td>${group.id}</td>
                    <td>${group.name}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deleteGroup(${group.id})">Удалить</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

async function loadSubjects() {
    try {
        const response = await fetch(`${API_BASE}/admin/subjects`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const subjects = await response.json();
            const tbody = document.getElementById('subjectsTableBody');
            tbody.innerHTML = subjects.map(subject => `
                <tr>
                    <td>${subject.id}</td>
                    <td>${subject.name}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deleteSubject(${subject.id})">Удалить</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading subjects:', error);
    }
}

async function loadAssignmentData() {
    try {
        const [teachersRes, subjectsRes, groupsRes] = await Promise.all([
            fetch(`${API_BASE}/admin/teachers`, { headers: getAuthHeaders() }),
            fetch(`${API_BASE}/admin/subjects`, { headers: getAuthHeaders() }),
            fetch(`${API_BASE}/admin/groups`, { headers: getAuthHeaders() })
        ]);

        if (teachersRes.ok) {
            const teachers = await teachersRes.json();
            const select = document.getElementById('assignmentTeacher');
            if (select) {
                select.innerHTML = '<option value="">Выберите учителя</option>';
                if (teachers.length === 0) {
                    select.innerHTML += '<option value="" disabled>Нет учителей. Создайте учителя в разделе "Создать пользователя"</option>';
                } else {
                    teachers.forEach(teacher => {
                        const option = document.createElement('option');
                        option.value = teacher.id;
                        let userName = 'Учитель #' + teacher.id;
                        if (teacher.user) {
                            userName = `${teacher.user.firstName || ''} ${teacher.user.lastName || ''}`.trim() || teacher.user.username || userName;
                        }
                        option.textContent = userName;
                        select.appendChild(option);
                    });
                }
            }
        } else {
            console.error('Failed to load teachers:', teachersRes.status);
            const select = document.getElementById('assignmentTeacher');
            if (select) {
                select.innerHTML = '<option value="">Ошибка загрузки учителей</option>';
            }
        }

        if (subjectsRes.ok) {
            const subjects = await subjectsRes.json();
            const select = document.getElementById('assignmentSubject');
            if (select) {
                select.innerHTML = '<option value="">Выберите предмет</option>';
                if (subjects.length === 0) {
                    select.innerHTML += '<option value="" disabled>Нет предметов. Создайте предмет в разделе "Предметы"</option>';
                } else {
                    subjects.forEach(subject => {
                        const option = document.createElement('option');
                        option.value = subject.id;
                        option.textContent = subject.name;
                        select.appendChild(option);
                    });
                }
            }
        } else {
            console.error('Failed to load subjects:', subjectsRes.status);
        }

        if (groupsRes.ok) {
            const groups = await groupsRes.json();
            const select = document.getElementById('assignmentGroup');
            if (select) {
                select.innerHTML = '<option value="">Выберите группу</option>';
                if (groups.length === 0) {
                    select.innerHTML += '<option value="" disabled>Нет групп. Создайте группу в разделе "Группы"</option>';
                } else {
                    groups.forEach(group => {
                        const option = document.createElement('option');
                        option.value = group.id;
                        option.textContent = group.name;
                        select.appendChild(option);
                    });
                }
            }
        } else {
            console.error('Failed to load groups:', groupsRes.status);
        }
    } catch (error) {
        console.error('Error loading assignment data:', error);
    }
}

document.getElementById('addGroupForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('groupName').value;
    try {
        const response = await fetch(`${API_BASE}/admin/groups`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ name })
        });
        if (response.ok) {
            closeModal('addGroupModal');
            loadGroups();
            document.getElementById('addGroupForm').reset();
        }
    } catch (error) {
        console.error('Error creating group:', error);
    }
});

document.getElementById('addSubjectForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('subjectName').value;
    try {
        const response = await fetch(`${API_BASE}/admin/subjects`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ name })
        });
        if (response.ok) {
            closeModal('addSubjectModal');
            loadSubjects();
            document.getElementById('addSubjectForm').reset();
        }
    } catch (error) {
        console.error('Error creating subject:', error);
    }
});

document.getElementById('assignmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const teacherId = document.getElementById('assignmentTeacher').value;
    const subjectId = document.getElementById('assignmentSubject').value;
    const groupId = document.getElementById('assignmentGroup').value;
    
    if (!teacherId || !subjectId || !groupId) {
        alert('Заполните все поля');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/admin/assign-teacher?teacherId=${teacherId}&subjectId=${subjectId}&groupId=${groupId}`, {
            method: 'POST',
            headers: getAuthHeaders()
        });
        if (response.ok) {
            alert('Учитель успешно назначен!');
            document.getElementById('assignmentForm').reset();
            // Перезагружаем данные
            loadAssignmentData();
        } else {
            const error = await response.json().catch(() => ({message: 'Ошибка назначения учителя'}));
            alert('Ошибка: ' + (error.message || 'Не удалось назначить учителя'));
        }
    } catch (error) {
        console.error('Error assigning teacher:', error);
        alert('Ошибка при назначении учителя: ' + error.message);
    }
});

function showAddGroupModal() {
    showModal('addGroupModal');
}

function showAddSubjectModal() {
    showModal('addSubjectModal');
}

async function deleteUser(id) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        try {
            const response = await fetch(`${API_BASE}/admin/users/${id}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if (response.ok) {
                loadUsers();
            }
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    }
}

async function deleteGroup(id) {
    if (confirm('Вы уверены, что хотите удалить эту группу?')) {
        // TODO: Implement delete group endpoint
        alert('Функция удаления группы будет реализована');
    }
}

async function deleteSubject(id) {
    if (confirm('Вы уверены, что хотите удалить этот предмет?')) {
        // TODO: Implement delete subject endpoint
        alert('Функция удаления предмета будет реализована');
    }
}

function toggleRoleFields() {
    const role = document.getElementById('regRole').value;
    const groupIdGroup = document.getElementById('groupIdGroup');
    if (role === 'STUDENT') {
        groupIdGroup.style.display = 'block';
        loadGroupsForRegister();
    } else {
        groupIdGroup.style.display = 'none';
    }
}

async function loadGroupsForRegister() {
    try {
        const response = await fetch(`${API_BASE}/admin/groups`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const groups = await response.json();
            const select = document.getElementById('regGroupId');
            select.innerHTML = '<option value="">Выберите группу</option>';
            groups.forEach(group => {
                const option = document.createElement('option');
                option.value = group.id;
                option.textContent = group.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const role = document.getElementById('regRole').value;
    const registerData = {
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value,
        firstName: document.getElementById('regFirstName').value,
        lastName: document.getElementById('regLastName').value,
        email: document.getElementById('regEmail').value,
        role: role
    };

    if (role === 'STUDENT') {
        const groupId = document.getElementById('regGroupId').value;
        if (!groupId) {
            alert('Выберите группу для ученика');
            return;
        }
        registerData.groupId = parseInt(groupId);
    } else if (role === 'TEACHER') {
        registerData.hireDate = new Date().toISOString().split('T')[0];
    }

    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(registerData)
        });

        if (response.ok) {
            alert('Пользователь успешно создан!');
            document.getElementById('registerForm').reset();
            toggleRoleFields();
            loadUsers();
        } else {
            const error = await response.json();
            alert('Ошибка: ' + (error.message || 'Не удалось создать пользователя'));
        }
    } catch (error) {
        console.error('Error registering user:', error);
        alert('Ошибка при создании пользователя');
    }
});

function toggleNotificationFields() {
    const type = document.getElementById('notificationType').value;
    document.getElementById('notificationRoleGroup').style.display = type === 'role' ? 'block' : 'none';
    document.getElementById('notificationUserGroup').style.display = type === 'user' ? 'block' : 'none';
    document.getElementById('notificationGroupGroup').style.display = type === 'group' ? 'block' : 'none';
    
    if (type === 'user') {
        loadUsersForNotification();
    } else if (type === 'group') {
        loadGroupsForNotification();
    }
}

async function loadUsersForNotification() {
    try {
        const response = await fetch(`${API_BASE}/admin/users`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const users = await response.json();
            const select = document.getElementById('notificationUser');
            select.innerHTML = '<option value="">Выберите пользователя</option>';
            users.forEach(user => {
                const option = document.createElement('option');
                option.value = user.id;
                option.textContent = `${user.firstName} ${user.lastName} (${user.username})`;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

async function loadGroupsForNotification() {
    try {
        const response = await fetch(`${API_BASE}/admin/groups`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const groups = await response.json();
            const select = document.getElementById('notificationGroup');
            select.innerHTML = '<option value="">Выберите группу</option>';
            groups.forEach(group => {
                const option = document.createElement('option');
                option.value = group.id;
                option.textContent = group.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

document.getElementById('notificationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const message = document.getElementById('notificationMessage').value;
    const type = document.getElementById('notificationType').value;
    
    let requestData = { message: message };
    
    if (type === 'role') {
        requestData.role = document.getElementById('notificationRole').value;
    } else if (type === 'user') {
        requestData.userId = parseInt(document.getElementById('notificationUser').value);
    } else if (type === 'group') {
        requestData.groupId = parseInt(document.getElementById('notificationGroup').value);
    }
    
    try {
        const response = await fetch(`${API_BASE}/notifications/send`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(requestData)
        });
        
        if (response.ok) {
            const result = await response.json();
            alert(result.message || 'Уведомление отправлено!');
            document.getElementById('notificationForm').reset();
            toggleNotificationFields();
        } else {
            const error = await response.json();
            alert('Ошибка: ' + (error.error || 'Не удалось отправить уведомление'));
        }
    } catch (error) {
        console.error('Error sending notification:', error);
        alert('Ошибка при отправке уведомления');
    }
});

