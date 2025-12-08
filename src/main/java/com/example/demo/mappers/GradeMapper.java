package com.example.demo.mappers;

import com.example.demo.models.Grade;
import com.example.demo.models.dtos.StudentGradesListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface GradeMapper {

    GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

    @Mapping(target = "subjectName", expression = "java(grade.getLesson().getSubject().getName())")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "gradeId", source = "id")
    StudentGradesListDto toStudentGradeDto(Grade grade);

    @Mapping(target = "gradeId", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "subjectName", expression = "java(grade.getLesson().getSubject().getName())")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "comment", source = "comment")
    StudentGradesListDto toStudentGradesListDto(Grade grade);
}
