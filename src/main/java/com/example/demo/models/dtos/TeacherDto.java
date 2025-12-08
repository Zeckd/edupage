package com.example.demo.models.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TeacherDto {
    private Long id;
    private Long userId;
    private LocalDate hireDate;
    private String description;
}
