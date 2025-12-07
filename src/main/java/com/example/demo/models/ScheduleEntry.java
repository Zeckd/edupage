package com.example.demo.models;
import com.example.demo.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

// Элемент расписания (шаблон)
@Entity
@Table(name = "schedule_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Weekday dayOfWeek;
    private LocalTime startTime;
    private int durationMinutes;
    private String room;
    private int lessonNumber;

    @ManyToOne
    @JoinColumn(name = "group_subject_teacher_id", nullable = false)
    private GroupSubjectTeacher groupSubjectTeacher;
}