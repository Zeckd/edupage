package com.example.demo.services;

import com.example.demo.models.Homework;

public interface HomeworkService {
    Homework setHomework(Long lessonId, String description, String deadline);
    Homework getLessonHomework(Long lessonId);
}
