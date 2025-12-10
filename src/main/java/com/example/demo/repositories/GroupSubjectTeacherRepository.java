package com.example.demo.repositories;

import com.example.demo.models.GroupSubjectTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupSubjectTeacherRepository extends JpaRepository<GroupSubjectTeacher, Long> {
    List<GroupSubjectTeacher> findByTeacherId(Long teacherId);
    List<GroupSubjectTeacher> findByGroupId(Long groupId);
}