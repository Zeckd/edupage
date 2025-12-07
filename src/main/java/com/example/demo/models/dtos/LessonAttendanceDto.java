package com.example.demo.models.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LessonAttendanceDto {
    private Long lessonId;
    private String subjectName;
    private String groupName;
    private LocalDateTime lessonDateTime;

    private List<StudentStatusDto> students;
}