package com.example.demo.models.dtos;

import com.example.demo.enums.AttendanceStatus;
import lombok.Data;

@Data
public class StudentStatusDto {
    private Long studentId;
    private String studentFullName;
    private AttendanceStatus status;
    private String comment;
}