package com.example.demo.mappers;

import com.example.demo.models.Student;
import com.example.demo.models.dtos.StudentDto;
import com.example.demo.models.dtos.StudentStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "studentId", source = "id")
    @Mapping(target = "studentFullName",
            expression = "java(student.getUser().getFirstName() + \" \" + student.getUser().getLastName())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "comment", ignore = true)
    StudentStatusDto toStudentStatusDto(Student student);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "groupName", source = "group.name")
    StudentDto toStudentDto(Student student);
}
