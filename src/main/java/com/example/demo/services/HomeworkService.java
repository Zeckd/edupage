package com.example.demo.services;

import com.example.demo.models.dtos.HomeworkDto;
import com.example.demo.models.dtos.HomeworkRequestDto;

public interface HomeworkService {

    HomeworkDto setHomework(Long lessonId, HomeworkRequestDto dto);

    HomeworkDto getHomeworkByLesson(Long lessonId);
}
