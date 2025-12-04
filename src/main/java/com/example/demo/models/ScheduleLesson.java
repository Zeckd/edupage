package com.example.demo.models;


import com.example.demo.enums.Weekday;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_lessons")
public class ScheduleLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_subject_id", nullable = false)
    private GroupSubject groupSubject;

    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    private Integer lessonIndex;

    private LocalTime startTime;

    private LocalTime endTime;

    @Column(length = 50)
    private String room;

    public ScheduleLesson() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GroupSubject getGroupSubject() { return groupSubject; }
    public void setGroupSubject(GroupSubject groupSubject) { this.groupSubject = groupSubject; }

    public Weekday getWeekday() { return weekday; }
    public void setWeekday(Weekday weekday) { this.weekday = weekday; }

    public Integer getLessonIndex() { return lessonIndex; }
    public void setLessonIndex(Integer lessonIndex) { this.lessonIndex = lessonIndex; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
