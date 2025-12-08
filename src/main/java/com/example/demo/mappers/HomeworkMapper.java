package com.example.demo.mappers;

import com.example.demo.models.Homework;
import com.example.demo.models.dtos.HomeworkDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    HomeworkMapper INSTANCE = Mappers.getMapper(HomeworkMapper.class);

    @Mapping(target = "lessonId", expression = "java(homework.getLesson() != null ? homework.getLesson().getId() : null)")
    HomeworkDto toDto(Homework homework);
}
