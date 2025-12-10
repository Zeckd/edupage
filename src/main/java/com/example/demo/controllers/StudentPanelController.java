package com.example.demo.controllers;

import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ScheduleService;
import com.example.demo.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
public class StudentPanelController {

    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @GetMapping("/profile")
    public ResponseEntity<Student> getMyProfile(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok(student);
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<?>> getMyAttendance(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok((List<?>) studentService.getStudentAttendance(student.getId()));
    }

    @GetMapping("/grades")
    public ResponseEntity<List<?>> getMyGrades(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok((List<?>) studentService.getStudentGrades(student.getId()));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleEntryDto>> getMySchedule(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok(scheduleService.getScheduleForGroup(student.getGroup().getId()));
    }
}

