package com.example.demo.controllers;

import com.example.demo.models.dtos.TeacherCreateDto;
import com.example.demo.models.dtos.TeacherDto;
import com.example.demo.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getAll();
    }

    @PostMapping
    public TeacherDto createTeacher(@RequestBody TeacherCreateDto dto) {
        return teacherService.create(dto);
    }

    @GetMapping("/user/{userId}")
    public TeacherDto getByUserId(@PathVariable Long userId) {
        return teacherService.getByUserId(userId);
    }
}
