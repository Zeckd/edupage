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

    @GetMapping("/profile")
    public ResponseEntity<Student> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(studentService.getProfile(authentication.getName()));
    }

    @GetMapping("/attendance")
    public ResponseEntity<?> getMyAttendance(Authentication authentication) {
        try {
            return ResponseEntity.ok(studentService.getAttendance(authentication.getName()));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/grades")
    public ResponseEntity<List<?>> getMyGrades(Authentication authentication) {
        return ResponseEntity.ok(studentService.getGrades(authentication.getName()));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleEntryDto>> getMySchedule(Authentication authentication) {
        return ResponseEntity.ok(studentService.getSchedule(authentication.getName()));
    }
}
