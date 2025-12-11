let teacherProfile = null;

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('userName').textContent = currentUser.username;
    loadProfile();
    loadLessons();
    loadLessonsForSelects();
    loadSchedule();
    updateNotificationBadge();
});

async function loadProfile() {
    try {
        const response = await fetch(`${API_BASE}/teacher/profile`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            teacherProfile = await response.json();
            loadSchedule();
        }
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

async function loadSchedule() {
    try {
        const response = await fetch(`${API_BASE}/teacher/schedule`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const schedule = await response.json();
            displaySchedule(schedule);
        }
    } catch (error) {
        console.error('Error loading schedule:', error);
    }
}

function displaySchedule(schedule) {
    const content = document.getElementById('scheduleContent');
    if (schedule.length === 0) {
        content.innerHTML = '<p>Нет запланированных уроков</p>';
        return;
    }
    
    const scheduleHtml = schedule.map(entry => `
        <div class="schedule-item">
            <strong>${entry.dayOfWeek}</strong> - ${entry.startTime}
            <p>${entry.subjectName} - ${entry.groupName}</p>
            <p>Кабинет: ${entry.room || 'N/A'}</p>
        </div>
    `).join('');
    content.innerHTML = scheduleHtml;
}

async function loadLessons() {
    try {
        const response = await fetch(`${API_BASE}/teacher/lessons`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const lessons = await response.json();
            const tbody = document.getElementById('lessonsTableBody');
            tbody.innerHTML = lessons.map(lesson => `
                <tr>
                    <td>${new Date(lesson.lessonDateTime).toLocaleString('ru-RU')}</td>
                    <td>${lesson.subjectName}</td>
                    <td>${lesson.groupName}</td>
                    <td>${lesson.durationMinutes} мин</td>
                    <td>
                        <button class="btn btn-success" onclick="viewLesson(${lesson.id})">Просмотр</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading lessons:', error);
    }
}

async function loadLessonsForSelects() {
    try {
        const response = await fetch(`${API_BASE}/teacher/lessons`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const lessons = await response.json();
            
            // Заполняем селекты для оценок, посещаемости и домашних заданий
            const gradeSelect = document.getElementById('gradeLessonSelect');
            const attendanceSelect = document.getElementById('attendanceLessonSelect');
            const homeworkSelect = document.getElementById('homeworkLessonSelect');
            
            const options = lessons.map(lesson => 
                `<option value="${lesson.id}">${new Date(lesson.lessonDateTime).toLocaleString('ru-RU')} - ${lesson.subjectName}</option>`
            ).join('');
            
            gradeSelect.innerHTML = '<option value="">Выберите урок</option>' + options;
            attendanceSelect.innerHTML = '<option value="">Выберите урок</option>' + options;
            homeworkSelect.innerHTML = '<option value="">Выберите урок</option>' + options;
            
            gradeSelect.addEventListener('change', loadGradesForLesson);
            attendanceSelect.addEventListener('change', loadAttendanceForLesson);
            homeworkSelect.addEventListener('change', loadHomeworkForLesson);
        }
    } catch (error) {
        console.error('Error loading lessons for selects:', error);
    }
}

async function loadGradesForLesson() {
    const lessonId = document.getElementById('gradeLessonSelect').value;
    if (!lessonId) return;
    
    try {
        const response = await fetch(`${API_BASE}/lessons/${lessonId}`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const lesson = await response.json();
            // Загружаем посещаемость для получения списка студентов
            const attendanceRes = await fetch(`${API_BASE}/attendance/lesson/${lessonId}`, {
                headers: getAuthHeaders()
            });
            if (attendanceRes.ok) {
                const attendance = await attendanceRes.json();
                displayGradesForm(lessonId, attendance.students || []);
            } else {
                document.getElementById('gradesContent').innerHTML = '<p>Сначала отметьте посещаемость для этого урока</p>';
            }
        }
    } catch (error) {
        console.error('Error loading grades:', error);
    }
}

function displayGradesForm(lessonId, students) {
    const content = document.getElementById('gradesContent');
    if (students.length === 0) {
        content.innerHTML = '<p>Нет студентов для этого урока</p>';
        return;
    }
    
    const html = `
        <table class="table">
            <thead>
                <tr>
                    <th>Студент</th>
                    <th>Оценка</th>
                    <th>Тип</th>
                    <th>Комментарий</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                ${students.map(student => {
                    const fullName = student.studentFullName || (student.firstName + ' ' + student.lastName) || 'Студент';
                    return `
                    <tr>
                        <td>${fullName}</td>
                        <td>
                            <input type="number" id="grade_${student.studentId}" min="1" max="5" style="width: 60px;">
                        </td>
                        <td>
                            <select id="gradeType_${student.studentId}" style="width: 120px;">
                                <option value="Обычная">Обычная</option>
                                <option value="Контрольная">Контрольная</option>
                                <option value="Самостоятельная">Самостоятельная</option>
                                <option value="Домашняя работа">Домашняя работа</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" id="gradeComment_${student.studentId}" placeholder="Комментарий" style="width: 200px;">
                        </td>
                        <td>
                            <button class="btn btn-success" onclick="saveGrade(${lessonId}, ${student.studentId})">Сохранить</button>
                        </td>
                    </tr>
                `;
                }).join('')}
            </tbody>
        </table>
    `;
    content.innerHTML = html;
}

async function saveGrade(lessonId, studentId) {
    const value = document.getElementById(`grade_${studentId}`).value;
    const type = document.getElementById(`gradeType_${studentId}`).value;
    const comment = document.getElementById(`gradeComment_${studentId}`).value;
    
    if (!value) {
        alert('Введите оценку');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/grades/add`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                lessonId: lessonId,
                studentId: studentId,
                value: parseInt(value),
                type: type,
                comment: comment
            })
        });
        if (response.ok) {
            alert('Оценка сохранена!');
        } else {
            alert('Ошибка при сохранении оценки');
        }
    } catch (error) {
        console.error('Error saving grade:', error);
        alert('Ошибка при сохранении оценки');
    }
}

async function loadAttendanceForLesson() {
    const lessonId = document.getElementById('attendanceLessonSelect').value;
    if (!lessonId) return;
    
    try {
        const response = await fetch(`${API_BASE}/attendance/lesson/${lessonId}`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const attendance = await response.json();
            displayAttendance(attendance);
        }
    } catch (error) {
        console.error('Error loading attendance:', error);
    }
}

function displayAttendance(attendance) {
    const content = document.getElementById('attendanceContent');
    if (!attendance.students || attendance.students.length === 0) {
        content.innerHTML = '<p>Нет данных о посещаемости</p>';
        return;
    }
    
    const html = `
        <table class="table">
            <thead>
                <tr>
                    <th>Студент</th>
                    <th>Статус</th>
                    <th>Комментарий</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                ${attendance.students.map(student => {
                    const fullName = student.studentFullName || (student.firstName + ' ' + student.lastName) || 'Студент';
                    return `
                    <tr>
                        <td>${fullName}</td>
                        <td>
                            <select id="attStatus_${student.studentId}" onchange="updateAttendanceStatus(${attendance.lessonId}, ${student.studentId})">
                                <option value="PRESENT" ${student.status === 'PRESENT' ? 'selected' : ''}>Присутствовал</option>
                                <option value="ABSENT" ${student.status === 'ABSENT' ? 'selected' : ''}>Отсутствовал</option>
                                <option value="LATE" ${student.status === 'LATE' ? 'selected' : ''}>Опоздал</option>
                                <option value="EXCUSED" ${student.status === 'EXCUSED' ? 'selected' : ''}>Уважительная причина</option>
                            </select>
                        </td>
                        <td>
                            <input type="text" id="attComment_${student.studentId}" value="${student.comment || ''}" 
                                   placeholder="Комментарий" onchange="updateAttendanceStatus(${attendance.lessonId}, ${student.studentId})">
                        </td>
                        <td>
                            <button class="btn btn-success" onclick="saveAttendance(${attendance.lessonId}, ${student.studentId})">Сохранить</button>
                        </td>
                    </tr>
                `;
                }).join('')}
            </tbody>
        </table>
    `;
    content.innerHTML = html;
}

function updateAttendanceStatus(lessonId, studentId) {
    // Автоматически сохраняем при изменении
    saveAttendance(lessonId, studentId);
}

async function saveAttendance(lessonId, studentId) {
    const status = document.getElementById(`attStatus_${studentId}`).value;
    const comment = document.getElementById(`attComment_${studentId}`).value;
    
    try {
        const response = await fetch(`${API_BASE}/attendance/set`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                lessonId: lessonId,
                students: [{
                    studentId: studentId,
                    status: status,
                    comment: comment
                }]
            })
        });
        if (response.ok) {
            // Обновляем без перезагрузки страницы
            loadAttendanceForLesson();
        }
    } catch (error) {
        console.error('Error saving attendance:', error);
    }
}

async function loadHomeworkForLesson() {
    const lessonId = document.getElementById('homeworkLessonSelect').value;
    if (!lessonId) return;
    
    try {
        const response = await fetch(`${API_BASE}/homework/lesson/${lessonId}`, {
            headers: getAuthHeaders()
        });
        if (response.ok) {
            const text = await response.text();
            if (text && text.trim()) {
                const homework = JSON.parse(text);
                displayHomework(homework);
            } else {
                document.getElementById('homeworkContent').innerHTML = `
                    <p>Домашнее задание не задано</p>
                    <button class="btn btn-success" onclick="setHomework(${lessonId})">Задать домашнее задание</button>
                `;
            }
        } else if (response.status === 404) {
            document.getElementById('homeworkContent').innerHTML = `
                <p>Домашнее задание не задано</p>
                <button class="btn btn-success" onclick="setHomework(${lessonId})">Задать домашнее задание</button>
            `;
        }
    } catch (error) {
        console.error('Error loading homework:', error);
        document.getElementById('homeworkContent').innerHTML = `
            <p>Домашнее задание не задано</p>
            <button class="btn btn-success" onclick="setHomework(${lessonId})">Задать домашнее задание</button>
        `;
    }
}

function displayHomework(homework) {
    const content = document.getElementById('homeworkContent');
    content.innerHTML = `
        <div class="homework-item">
            <p><strong>Описание:</strong> ${homework.description}</p>
            <p><strong>Срок сдачи:</strong> ${new Date(homework.deadline).toLocaleString('ru-RU')}</p>
            <button class="btn btn-success" onclick="editHomework(${homework.id})">Изменить</button>
        </div>
    `;
}

function showAddLessonModal() {
    loadGroupsAndSubjects();
    showModal('addLessonModal');
}

async function loadGroupsAndSubjects() {
    try {
        const [groupsRes, subjectsRes] = await Promise.all([
            fetch(`${API_BASE}/groups`, { headers: getAuthHeaders() }),
            fetch(`${API_BASE}/subjects`, { headers: getAuthHeaders() })
        ]);
        
        if (groupsRes.ok) {
            const groups = await groupsRes.json();
            const select = document.getElementById('lessonGroup');
            select.innerHTML = '<option value="">Выберите группу</option>';
            groups.forEach(group => {
                const option = document.createElement('option');
                option.value = group.id;
                option.textContent = group.name;
                select.appendChild(option);
            });
        }
        
        if (subjectsRes.ok) {
            const subjects = await subjectsRes.json();
            const select = document.getElementById('lessonSubject');
            select.innerHTML = '<option value="">Выберите предмет</option>';
            subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject.id;
                option.textContent = subject.name;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading groups and subjects:', error);
    }
}

document.getElementById('addLessonForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const lessonDateTime = document.getElementById('lessonDateTime').value;
    if (!lessonDateTime) {
        alert('Выберите дату и время урока');
        return;
    }
    
    const lessonData = {
        lessonDateTime: lessonDateTime,
        durationMinutes: parseInt(document.getElementById('lessonDuration').value) || 45,
        groupId: parseInt(document.getElementById('lessonGroup').value),
        subjectId: parseInt(document.getElementById('lessonSubject').value)
        // scheduleEntryId не обязателен - можно не указывать
    };
    
    try {
        const response = await fetch(`${API_BASE}/teacher/lessons`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(lessonData)
        });
        
        if (response.ok) {
            alert('Урок успешно создан!');
            closeModal('addLessonModal');
            loadLessons();
            loadLessonsForSelects();
            loadSchedule();
            document.getElementById('addLessonForm').reset();
        } else if (response.status === 401 || response.status === 403) {
            // Токен невалиден или нет доступа - редирект на авторизацию
            localStorage.removeItem('token');
            localStorage.removeItem('currentUser');
            alert('Сессия истекла. Пожалуйста, войдите снова.');
            window.location.href = '/index.html';
        } else {
            // Другие ошибки сервера (400, 500 и т.д.)
            const error = await response.json().catch(() => ({message: 'Не удалось создать урок'}));
            alert('Ошибка: ' + (error.message || 'Не удалось создать урок. Проверьте, что все поля заполнены правильно.'));
            console.error('Error creating lesson:', response.status, error);
        }
    } catch (error) {
        console.error('Error creating lesson:', error);
        alert('Ошибка при создании урока: ' + error.message);
    }
});

function viewLesson(lessonId) {
    // TODO: Показать детали урока
    alert('Просмотр урока: ' + lessonId);
}


function setHomework(lessonId) {
    const description = prompt('Введите описание домашнего задания:');
    const deadlineInput = prompt('Введите срок сдачи (YYYY-MM-DD HH:mm):');
    
    if (description && deadlineInput) {
        let deadline = deadlineInput;
        if (!deadline.includes('T')) {
            deadline = deadline.replace(' ', 'T');
        }
        if (!deadline.includes(':')) {
            deadline += ':00';
        }
        
        fetch(`${API_BASE}/homework/set/${lessonId}`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({
                description: description,
                deadline: deadline
            })
        }).then(response => {
            if (response.ok) {
                alert('Домашнее задание успешно задано!');
                loadHomeworkForLesson(lessonId);
            } else {
                response.json().then(error => {
                    alert('Ошибка: ' + (error.message || 'Не удалось задать домашнее задание'));
                }).catch(() => {
                    alert('Ошибка: Не удалось задать домашнее задание');
                });
            }
        }).catch(error => {
            console.error('Error setting homework:', error);
            alert('Ошибка при задании домашнего задания');
        });
    }
}

function editHomework(homeworkId) {
    // TODO: Редактирование домашнего задания
    alert('Редактирование домашнего задания');
}

