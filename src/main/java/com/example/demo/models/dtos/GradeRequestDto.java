package com.example.demo.models.dtos;
import lombok.Data;

@Data
public class GradeRequestDto {
    private Long lessonId;
    private Long studentId;
    private Integer value;
    private String comment;
    private String type;
}