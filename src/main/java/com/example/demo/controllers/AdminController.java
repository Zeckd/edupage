package com.example.demo.controllers;

import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.*;
import com.example.demo.models.dtos.RegisterRequestDto;
import com.example.demo.models.dtos.StudentDto;
import com.example.demo.models.dtos.TeacherDto;
import com.example.demo.repositories.*;
import com.example.demo.services.AdminService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final StudentRepository studentRepository;
    private final TeacherService teacherService;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final StudentMapper studentMapper = StudentMapper.INSTANCE;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll().stream()
                .map(studentMapper::toStudentDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupRepository.findAll());
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @PostMapping("/notify-all")
    public ResponseEntity<Void> notifyAll(@RequestParam String message) {
        adminService.notifyAllUsers(message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-teacher")
    public ResponseEntity<GroupSubjectTeacher> assignTeacherToGroup(
            @RequestParam Long teacherId,
            @RequestParam Long subjectId,
            @RequestParam Long groupId) {
        return ResponseEntity.ok(adminService.assignTeacherToGroup(teacherId, subjectId, groupId));
    }
}
