package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.models.dtos.RegisterRequestDto;
import com.example.demo.repositories.*;
import com.example.demo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final GroupSubjectTeacherRepository groupSubjectTeacherRepository;
    private final NotificationService notificationService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", userRepository.count());
        dashboard.put("totalStudents", studentRepository.count());
        dashboard.put("totalTeachers", teacherRepository.count());
        dashboard.put("totalGroups", groupRepository.count());
        dashboard.put("totalSubjects", subjectRepository.count());
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            if (student.getUser() != null) {
                student.getUser().getUsername();
            }
            if (student.getGroup() != null) {
                student.getGroup().getName();
            }
        });
        return ResponseEntity.ok(students);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        teachers.forEach(teacher -> {
            if (teacher.getUser() != null) {
                teacher.getUser().getUsername();
            }
        });
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupRepository.findAll());
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupRepository.save(group));
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectRepository.save(subject));
    }

    @PostMapping("/assign-teacher")
    public ResponseEntity<GroupSubjectTeacher> assignTeacherToGroup(
            @RequestParam Long teacherId,
            @RequestParam Long subjectId,
            @RequestParam Long groupId) {
        
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

        return ResponseEntity.ok(groupSubjectTeacherRepository.save(gst));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notify-all")
    public ResponseEntity<Void> notifyAll(@RequestParam String message) {
        notificationService.sendNotificationToRole("STUDENT", message);
        notificationService.sendNotificationToRole("TEACHER", message);
        return ResponseEntity.ok().build();
    }
}

