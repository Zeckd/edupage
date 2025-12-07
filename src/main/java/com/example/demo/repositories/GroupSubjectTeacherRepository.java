package com.example.demo.repositories;

import com.example.demo.models.GroupSubjectTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupSubjectTeacherRepository extends JpaRepository<GroupSubjectTeacher, Long> {
    // Найти все связки для данного учителя
    List<GroupSubjectTeacher> findByTeacherId(Long teacherId);
    // Найти все связки для данной группы
    List<GroupSubjectTeacher> findByGroupId(Long groupId);
}