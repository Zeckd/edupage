package com.example.demo.mappers;

import com.example.demo.models.Lesson;
import com.example.demo.models.ScheduleEntry;
import com.example.demo.models.dtos.LessonListDto;
import com.example.demo.models.dtos.ScheduleEntryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "scheduleId", expression = "java(entry.getId())")
    @Mapping(target = "subjectName", expression = "java(entry.getGroupSubjectTeacher().getSubject().getName())")
    @Mapping(target = "groupName", expression = "java(entry.getGroupSubjectTeacher().getGroup().getName())")
    @Mapping(target = "teacherFullName",
            expression = "java(entry.getGroupSubjectTeacher().getTeacher().getUser().getFirstName() + \" \" + entry.getGroupSubjectTeacher().getTeacher().getUser().getLastName())")
    @Mapping(target = "teacherUserId", expression = "java(entry.getGroupSubjectTeacher().getTeacher().getUser().getId())")
    ScheduleEntryDto toScheduleEntryDto(ScheduleEntry entry);


    @Mapping(target = "subjectName", expression = "java(lesson.getSubject().getName())")
    @Mapping(target = "groupName", expression = "java(lesson.getGroup().getName())")
    @Mapping(target = "teacherFullName",
            expression = "java(lesson.getTeacher().getUser().getFirstName() + \" \" + lesson.getTeacher().getUser().getLastName())")
    @Mapping(target = "attendanceTaken", ignore = true)
    @Mapping(target = "homeworkSet", ignore = true)
    LessonListDto toLessonListDto(Lesson lesson);
}
