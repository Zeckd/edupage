let studentProfile = null;

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('userName').textContent = currentUser.username;
    loadProfile();
    loadGrades();
    loadAttendance();
    loadHomework();
    updateNotificationBadge();
});

async function loadProfile() {
    try {
        const response = await fetch(`${API_BASE}/student/profile`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            studentProfile = await response.json();
            loadSchedule();
        }
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

async function loadSchedule() {
    try {
        const response = await fetch(`${API_BASE}/student/schedule`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const schedule = await response.json();
            if (schedule.length === 0) {
                document.getElementById('scheduleContent').innerHTML = '<p>Расписание не найдено</p>';
            } else {
                const scheduleHtml = schedule.map(entry => `
                    <div class="schedule-item">
                        <strong>${entry.dayOfWeek}</strong> - ${entry.startTime}
                        <p>${entry.subjectName} - ${entry.groupName}</p>
                        <p>Кабинет: ${entry.room || 'N/A'}</p>
                    </div>
                `).join('');
                document.getElementById('scheduleContent').innerHTML = scheduleHtml;
            }
        }
    } catch (error) {
        console.error('Error loading schedule:', error);
    }
}

async function loadGrades() {
    try {
        const response = await fetch(`${API_BASE}/student/grades`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const grades = await response.json();
            const tbody = document.getElementById('gradesTableBody');
            if (grades.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4">Нет оценок</td></tr>';
            } else {
                tbody.innerHTML = grades.map(grade => `
                    <tr>
                        <td>${grade.subjectName || 'N/A'}</td>
                        <td>${grade.value || 'N/A'}</td>
                        <td>${grade.createdAt ? new Date(grade.createdAt).toLocaleDateString('ru-RU') : 'N/A'}</td>
                        <td>${grade.comment || ''}</td>
                    </tr>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error loading grades:', error);
    }
}

async function loadAttendance() {
    try {
        const response = await fetch(`${API_BASE}/student/attendance`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const attendances = await response.json();
            const tbody = document.getElementById('attendanceTableBody');
            if (attendances.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4">Нет данных о посещаемости</td></tr>';
            } else {
                tbody.innerHTML = attendances.map(att => `
                    <tr>
                        <td>${att.lessonDateTime ? new Date(att.lessonDateTime).toLocaleDateString('ru-RU') : 'N/A'}</td>
                        <td>${att.subjectName || 'N/A'}</td>
                        <td>${att.status || 'N/A'}</td>
                        <td>${att.comment || ''}</td>
                    </tr>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error loading attendance:', error);
    }
}

async function loadHomework() {
    try {
        // Загружаем уроки студента и их домашние задания
        const response = await fetch(`${API_BASE}/student/profile`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const student = await response.json();
            // TODO: Загрузить домашние задания для группы студента
            document.getElementById('homeworkContent').innerHTML = '<p>Домашние задания будут отображаться здесь</p>';
        }
    } catch (error) {
        console.error('Error loading homework:', error);
    }
}

