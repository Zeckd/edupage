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

    private final LessonService lessonService;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final NotificationService notificationService;

    @GetMapping("/profile")
    public ResponseEntity<Teacher> getMyProfile(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<LessonDto>> getMyLessons(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return ResponseEntity.ok(lessonService.findByTeacher(teacher.getId()));
    }

    @PostMapping("/lessons")
    public ResponseEntity<LessonDto> createLesson(Authentication authentication, @RequestBody LessonCreateDto dto) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        // Убеждаемся, что урок создается от имени текущего учителя
        dto.setTeacherId(teacher.getId());
        LessonDto lesson = lessonService.create(dto);
        
        // Отправляем уведомление студентам группы
        notificationService.sendNotificationToRole("STUDENT", 
            "Новый урок назначен: " + lesson.getSubjectName());
        
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleEntryDto>> getMySchedule(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return ResponseEntity.ok(scheduleService.getScheduleForTeacher(teacher.getId()));
    }
}

