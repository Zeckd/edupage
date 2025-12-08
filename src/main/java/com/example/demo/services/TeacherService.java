package com.example.demo.services;

import com.example.demo.models.dtos.TeacherCreateDto;
import com.example.demo.models.dtos.TeacherDto;

import java.util.List;

public interface TeacherService {
    TeacherDto create(TeacherCreateDto dto);
    List<TeacherDto> getAll();
    TeacherDto getByUserId(Long userId);
}
