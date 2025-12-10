package com.example.demo.repositories;

import com.example.demo.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLessonId(Long lessonId);
    Optional<Attendance> findByLessonIdAndStudentId(Long lessonId, Long studentId);
    List<Attendance> findByStudentId(Long studentId);
}