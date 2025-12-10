package com.example.demo.repositories;

import com.example.demo.models.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
    List<ScheduleEntry> findByGroupSubjectTeacher_GroupId(Long groupId);

    List<ScheduleEntry> findByGroupSubjectTeacher_TeacherId(Long teacherId);
}