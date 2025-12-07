package com.example.demo.services.impl;

import com.example.demo.models.Lesson;
import com.example.demo.repositories.LessonRepository;
import com.example.demo.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public Lesson getLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    @Override
    public List<Lesson> getGroupLessons(Long groupId) {
        return lessonRepository.findByGroup_IdAndLessonDateTimeBetween(
                groupId,
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1)
        );
    }

    @Override
    public List<Lesson> getTeacherLessons(Long teacherId) {
        return lessonRepository.findByTeacher_IdAndLessonDateTimeBetween(
                teacherId,
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1)
        );
    }
}
