package com.example.demo.models;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "group_subject_teacher")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GroupSubjectTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "groupSubjectTeacher")
    private List<ScheduleEntry> scheduleEntries;
}