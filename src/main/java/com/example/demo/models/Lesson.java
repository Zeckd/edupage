package com.example.demo.models;



import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lessonDateTime;
    private int durationMinutes;

    @ManyToOne
    @JsonIgnore
    private ScheduleEntry scheduleEntry;

    @ManyToOne
    @JsonIgnore
    private Group group;

    @ManyToOne
    @JsonIgnore
    private Subject subject;

    @ManyToOne
    @JsonIgnore
    private Teacher teacher;

    @OneToMany(mappedBy = "lesson")
    @JsonIgnore
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "lesson")
    @JsonIgnore
    private List<Grade> grades;

    @OneToOne(mappedBy = "lesson")
    @JsonIgnore
    private Homework homework;
}