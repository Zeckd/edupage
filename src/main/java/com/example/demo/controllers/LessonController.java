package com.example.demo.controllers;

import com.example.demo.models.dtos.LessonCreateDto;
import com.example.demo.models.dtos.LessonDto;
import com.example.demo.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public LessonDto create(@RequestBody LessonCreateDto dto) {
        return lessonService.create(dto);
    }

    @GetMapping("/{id}")
    public LessonDto find(@PathVariable Long id) {
        return lessonService.findById(id);
    }

    @GetMapping("/group/{groupId}")
    public List<LessonDto> getByGroup(@PathVariable Long groupId) {
        return lessonService.findByGroup(groupId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<LessonDto> getByTeacher(@PathVariable Long teacherId) {
        return lessonService.findByTeacher(teacherId);
    }
}
