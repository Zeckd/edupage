package com.example.demo.models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDateTime createdAt;

    @ManyToOne
    private User recipient; // Уведомление для конкретного пользователя (Учителя/Студента)

    private boolean readFlag;
}