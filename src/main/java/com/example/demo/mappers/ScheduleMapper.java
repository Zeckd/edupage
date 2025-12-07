package com.example.demo.mappers;
import com.example.demo.models.dtos.LessonListDto;
import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.models.Lesson;
import com.example.demo.models.ScheduleEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "subjectName", source = "entry.groupSubjectTeacher.subject.name")
    @Mapping(target = "teacherFullName", expression = "java(entry.getGroupSubjectTeacher().getTeacher().getUser().getFirstName() + \" \" + entry.getGroupSubjectTeacher().getTeacher().getUser().getLastName())")
    @Mapping(target = "teacherUserId", source = "entry.groupSubjectTeacher.teacher.user.id")
    @Mapping(target = "groupName", source = "entry.groupSubjectTeacher.group.name")
    @Mapping(target = "scheduleId", source = "entry.id")
    ScheduleEntryDto toScheduleEntryDto(ScheduleEntry entry);


    @Mapping(target = "subjectName", source = "lesson.subject.name")
    @Mapping(target = "groupName", source = "lesson.group.name")
    @Mapping(target = "teacherFullName", expression = "java(lesson.getTeacher().getUser().getFirstName() + \" \" + lesson.getTeacher().getUser().getLastName())")
    @Mapping(target = "attendanceTaken", ignore = true)
    @Mapping(target = "homeworkSet", ignore = true)
    LessonListDto toLessonListDto(Lesson lesson);
}