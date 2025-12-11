package com.example.demo.mappers;

import com.example.demo.models.Attendance;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.models.dtos.LessonAttendanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentFullName",
            expression = "java(attendance.getStudent().getUser().getFirstName() + \" \" + attendance.getStudent().getUser().getLastName())")
    StudentStatusDto toStudentStatusDto(Attendance attendance);

    List<StudentStatusDto> toStudentStatusList(List<Attendance> attendanceList);

    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "subjectName", source = "lesson.subject.name")
    @Mapping(target = "groupName", source = "lesson.group.name")
    @Mapping(target = "lessonDateTime", source = "lesson.lessonDateTime")
    LessonAttendanceDto toLessonAttendanceDto(Attendance attendance);

    @Mapping(target = "lessonDateTime", source = "lesson.lessonDateTime")
    @Mapping(target = "subjectName", source = "lesson.subject.name")
    @Mapping(target = "teacherFullName",
            expression = "java(att.getLesson().getTeacher().getUser().getFirstName() + \" \" + att.getLesson().getTeacher().getUser().getLastName())")
    StudentAttendanceReportDto toStudentAttendanceReportDto(Attendance att);
}
