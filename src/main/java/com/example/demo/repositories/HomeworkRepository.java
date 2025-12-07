package com.example.demo.repositories;

import com.example.demo.models.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    Optional<Homework> findByLessonId(Long lessonId);
}