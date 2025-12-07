package com.example.demo.mappers;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentGradesListDto;
import com.example.demo.models.Attendance;
import com.example.demo.models.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeAttendanceMapper {


    @Mapping(target = "lessonDateTime", source = "attendance.lesson.lessonDateTime")
    @Mapping(target = "subjectName", source = "attendance.lesson.subject.name")
    @Mapping(target = "teacherFullName", expression = "java(attendance.getLesson().getTeacher().getUser().getFirstName() + \" \" + attendance.getLesson().getTeacher().getUser().getLastName())")
    StudentAttendanceReportDto toAttendanceReportDto(Attendance attendance);


    @Mapping(target = "subjectName", source = "grade.lesson.subject.name")
    @Mapping(target = "gradeId", source = "grade.id")
    StudentGradesListDto toStudentGradesListDto(Grade grade);
}