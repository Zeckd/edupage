package com.example.demo.models.dtos;
import com.example.demo.enums.Weekday;
import lombok.Data;
import java.time.LocalTime;

@Data
public class ScheduleEntryDto {
    private Long scheduleId;
    private Weekday dayOfWeek;
    private LocalTime startTime;
    private int durationMinutes;
    private String room;
    private int lessonNumber;

    private String subjectName;
    private String teacherFullName;
    private Long teacherUserId;
    private String groupName;
}