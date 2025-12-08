package com.example.demo.mappers;

import com.example.demo.models.Student;
import com.example.demo.models.dtos.StudentStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "studentId", source = "id")
    @Mapping(target = "studentFullName",
            expression = "java(student.getUser().getFirstName() + \" \" + student.getUser().getLastName())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comment", ignore = true)
    StudentStatusDto toStudentStatusDto(Student student);
}


