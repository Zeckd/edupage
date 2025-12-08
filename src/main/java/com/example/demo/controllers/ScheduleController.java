package com.example.demo.controllers;

import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/group/{groupId}")
    public List<ScheduleEntryDto> getGroupSchedule(@PathVariable Long groupId) {
        return scheduleService.getScheduleForGroup(groupId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<ScheduleEntryDto> getTeacherSchedule(@PathVariable Long teacherId) {
        return scheduleService.getScheduleForTeacher(teacherId);
    }
}
