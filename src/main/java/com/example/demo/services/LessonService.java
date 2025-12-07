package com.example.demo.services;

import com.example.demo.models.Lesson;
import java.util.List;

public interface LessonService {
    Lesson getLesson(Long id);
    List<Lesson> getGroupLessons(Long groupId);
    List<Lesson> getTeacherLessons(Long teacherId);
}
