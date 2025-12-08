package com.example.demo.models.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonListDto {
    private Long lessonId;
    private LocalDateTime lessonDateTime;
    private String subjectName;
    private String groupName;
    private String teacherFullName;
    private String room;
    private boolean attendanceTaken;
    private boolean homeworkSet;
}