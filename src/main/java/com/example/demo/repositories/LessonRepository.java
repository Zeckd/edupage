package com.example.demo.repositories;

import com.example.demo.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByGroup_IdAndLessonDateTimeBetween(Long groupId, LocalDateTime start, LocalDateTime end);
    List<Lesson> findByTeacher_IdAndLessonDateTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);
}