package com.example.demo.mappers;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentGradesListDto;
import com.example.demo.models.Attendance;
import com.example.demo.models.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeAttendanceMapper {
}