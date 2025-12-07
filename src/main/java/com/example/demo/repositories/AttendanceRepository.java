package com.example.demo.repositories;

import com.example.demo.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Найти посещаемость для конкретного урока
    List<Attendance> findByLessonId(Long lessonId);
    // Найти запись о посещаемости студента на конкретном уроке
    Optional<Attendance> findByLessonIdAndStudentId(Long lessonId, Long studentId);
    // Найти всю посещаемость студента
    List<Attendance> findByStudentId(Long studentId);
}