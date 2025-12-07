package com.example.demo.models.dtos;
import com.example.demo.enums.AttendanceStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentAttendanceReportDto {
    private LocalDateTime lessonDateTime;
    private String subjectName;
    private String teacherFullName;
    private AttendanceStatus status;
    private String comment;
}