package com.example.demo.models.dtos;

import com.example.demo.enums.AttendanceStatus;
import lombok.Data;
import java.util.List;

@Data
public class AttendanceRequestDto {
    private Long lessonId;
    private List<StudentAttendanceDto> students;

    @Data
    public static class StudentAttendanceDto {
        private Long studentId;
        private AttendanceStatus status;
        private String comment;
    }
}