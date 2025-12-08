package com.example.demo.models.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomeworkDto {
    private Long id;
    private Long lessonId;
    private String description;
    private LocalDateTime deadline;
}
