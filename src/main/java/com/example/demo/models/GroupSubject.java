package com.example.demo.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "group_subjects")
public class GroupSubject {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacherId;
    @ManyToMany
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "group_subject_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjectId;
    @OneToOne
    @JoinColumn(name = "group_id")
    private Group groupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public List<Subject> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<Subject> subjectId) {
        this.subjectId = subjectId;
    }

    public Group getGroupId() {
        return groupId;
    }

    public void setGroupId(Group groupId) {
        this.groupId = groupId;
    }
}
