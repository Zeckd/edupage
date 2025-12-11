package com.example.demo.services.impl;

import com.example.demo.mappers.HomeworkMapper;
import com.example.demo.models.Homework;
import com.example.demo.models.Lesson;
import com.example.demo.models.dtos.HomeworkDto;
import com.example.demo.models.dtos.HomeworkRequestDto;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.LessonRepository;
import com.example.demo.services.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;

    private final HomeworkMapper mapper = HomeworkMapper.INSTANCE;

    @Override
    public HomeworkDto setHomework(Long lessonId, HomeworkRequestDto dto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Homework hw = Homework.builder()
                .lesson(lesson)
                .description(dto.getDescription())
                .deadline(LocalDateTime.parse(dto.getDeadline()))
                .build();

        hw = homeworkRepository.save(hw);
        return mapper.toDto(hw);
    }

    @Override
    public HomeworkDto getHomeworkByLesson(Long lessonId) {
        return homeworkRepository.findByLessonId(lessonId)
                .map(mapper::toDto)
                .orElse(null);
    }
}
