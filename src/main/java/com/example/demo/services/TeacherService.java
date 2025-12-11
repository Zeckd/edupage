package com.example.demo.services;

import com.example.demo.models.Teacher;
import com.example.demo.models.dtos.*;

import java.util.List;

public interface TeacherService {
    TeacherDto create(TeacherCreateDto dto);
    List<TeacherDto> getAll();
    TeacherDto getByUserId(Long userId);

    Teacher getProfile(String name);

    List<LessonDto> getLessons(String name);

    LessonDto createLesson(String name, LessonCreateDto dto);

    List<ScheduleEntryDto> getSchedule(String name);
}
