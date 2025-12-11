package com.example.demo.services;

import com.example.demo.models.GroupSubjectTeacher;
import com.example.demo.models.User;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Map<String, Object> getDashboardStats();

    List<User> getAllUsers();

    void notifyAllUsers(String message);

    GroupSubjectTeacher assignTeacherToGroup(Long teacherId, Long subjectId, Long groupId);
}
