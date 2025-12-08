package com.example.demo.mappers;

import com.example.demo.models.Lesson;
import com.example.demo.models.dtos.LessonCreateDto;
import com.example.demo.models.dtos.LessonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    Lesson lessonCreateDtoToLesson(LessonCreateDto dto);

    @Mapping(target = "groupName", expression = "java(lesson.getGroup() != null ? lesson.getGroup().getName() : null)")
    @Mapping(target = "subjectName", expression = "java(lesson.getSubject() != null ? lesson.getSubject().getName() : null)")
    @Mapping(target = "teacherName", expression = "java(lesson.getTeacher() != null ? lesson.getTeacher().getUser().getFirstName() + \" \" + lesson.getTeacher().getUser().getLastName() : null)")
    LessonDto lessonToLessonDto(Lesson lesson);
}
