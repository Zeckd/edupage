package com.example.demo.mappers;

import com.example.demo.models.Teacher;
import com.example.demo.models.dtos.TeacherCreateDto;
import com.example.demo.models.dtos.TeacherDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(source = "userId", target = "user.id")
    Teacher createDtoToTeacher(TeacherCreateDto dto);

    @Mapping(source = "user.id", target = "userId")
    TeacherDto teacherToDto(Teacher teacher);
}
