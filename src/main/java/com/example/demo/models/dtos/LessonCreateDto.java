package com.example.demo.models.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonCreateDto {
    private LocalDateTime lessonDateTime;
    private int durationMinutes;
    private Long scheduleEntryId;
    private Long groupId;
    private Long subjectId;
    private Long teacherId;
}
