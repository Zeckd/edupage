package com.example.demo.repositories;
import com.example.demo.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByLessonId(Long lessonId);
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByStudentIdAndLesson_Subject_Id(Long studentId, Long subjectId);
}
