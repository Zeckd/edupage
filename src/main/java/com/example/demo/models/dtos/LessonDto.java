package com.example.demo.models.dtos;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LessonDto {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private int durationMinutes;
    private String groupName;
    private String subjectName;
    private String teacherName;
}