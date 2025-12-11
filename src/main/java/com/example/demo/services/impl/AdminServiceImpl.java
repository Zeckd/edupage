package com.example.demo.services.impl;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import com.example.demo.services.AdminService;
import com.example.demo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final GroupSubjectTeacherRepository groupSubjectTeacherRepository;
    private final NotificationService notificationService;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", userRepository.count());
        dashboard.put("totalStudents", studentRepository.count());
        dashboard.put("totalTeachers", teacherRepository.count());
        dashboard.put("totalGroups", groupRepository.count());
        dashboard.put("totalSubjects", subjectRepository.count());
        return dashboard;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public GroupSubjectTeacher assignTeacherToGroup(Long teacherId, Long subjectId, Long groupId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupSubjectTeacher gst = GroupSubjectTeacher.builder()
                .teacher(teacher)
                .subject(subject)
                .group(group)
                .build();

        return groupSubjectTeacherRepository.save(gst);
    }

    public void notifyAllUsers(String message) {
        notificationService.sendNotificationToRole("STUDENT", message);
        notificationService.sendNotificationToRole("TEACHER", message);
    }
}
