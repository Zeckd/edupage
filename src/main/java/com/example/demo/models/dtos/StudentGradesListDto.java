package com.example.demo.models.dtos;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentGradesListDto {
    private Long gradeId;
    private LocalDateTime createdAt;
    private String subjectName;
    private Integer value;
    private String type;
    private String comment;
}