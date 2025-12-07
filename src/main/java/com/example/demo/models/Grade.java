package com.example.demo.models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Integer value;
    private String comment;
    private LocalDateTime createdAt;

    private String type;
}