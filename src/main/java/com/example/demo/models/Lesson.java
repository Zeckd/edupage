package com.example.demo.models;



import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// Факт проведенного занятия
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lessonDateTime;
    private int durationMinutes;

    @ManyToOne
    private ScheduleEntry scheduleEntry;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "lesson")
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "lesson")
    private List<Grade> grades;

    @OneToOne(mappedBy = "lesson")
    private Homework homework;
}