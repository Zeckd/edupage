package com.example.demo.services.impl;

import com.example.demo.models.Homework;
import com.example.demo.models.Lesson;
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

    @Override
    public Homework setHomework(Long lessonId, String description, String deadline) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Homework hw = Homework.builder()
                .lesson(lesson)
                .description(description)
                .deadline(LocalDateTime.parse(deadline))
                .build();

        return homeworkRepository.save(hw);
    }

    @Override
    public Homework getLessonHomework(Long lessonId) {
        return homeworkRepository.findByLessonId(lessonId)
                .orElse(null);
    }
}
