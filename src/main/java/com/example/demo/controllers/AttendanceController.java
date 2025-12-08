package com.example.demo.controllers;

import com.example.demo.models.dtos.AttendanceRequestDto;
import com.example.demo.models.dtos.LessonAttendanceDto;
import com.example.demo.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/set")
    public void set(@RequestBody AttendanceRequestDto dto) {
        attendanceService.setAttendance(dto);
    }

    @GetMapping("/lesson/{lessonId}")
    public LessonAttendanceDto getLesson(@PathVariable Long lessonId) {
        return attendanceService.getLessonAttendance(lessonId);
    }
}
