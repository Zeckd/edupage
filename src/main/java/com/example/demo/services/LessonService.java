package com.example.demo.services;

import com.example.demo.models.dtos.LessonCreateDto;
import com.example.demo.models.dtos.LessonDto;

import java.util.List;

public interface LessonService {

    LessonDto create(LessonCreateDto dto);

    LessonDto findById(Long id);

    List<LessonDto> findByGroup(Long groupId);

    List<LessonDto> findByTeacher(Long teacherId);
}
