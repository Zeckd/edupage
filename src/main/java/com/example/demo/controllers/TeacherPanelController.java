package com.example.demo.controllers;

import com.example.demo.models.Teacher;
import com.example.demo.models.User;
import com.example.demo.models.dtos.LessonCreateDto;
import com.example.demo.models.dtos.LessonDto;
import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.LessonService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.ScheduleService;
import com.example.demo.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
public class TeacherPanelController {

    private final TeacherService teacherService;

    @GetMapping("/profile")
    public ResponseEntity<Teacher> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(teacherService.getProfile(authentication.getName()));
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<LessonDto>> getMyLessons(Authentication authentication) {
        return ResponseEntity.ok(teacherService.getLessons(authentication.getName()));
    }

    @PostMapping("/lessons")
    public ResponseEntity<LessonDto> createLesson(
            Authentication authentication,
            @RequestBody LessonCreateDto dto) {
        return ResponseEntity.ok(teacherService.createLesson(authentication.getName(), dto));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleEntryDto>> getMySchedule(Authentication authentication) {
        return ResponseEntity.ok(teacherService.getSchedule(authentication.getName()));
    }
}
