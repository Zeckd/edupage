package com.example.demo.services;

import com.example.demo.models.Student;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentGradesListDto;

import java.util.List;

public interface StudentService {

    List<StudentStatusDto> getStudentsByGroup(Long groupId);

    List<StudentAttendanceReportDto> getStudentAttendance(Long studentId);

    List<StudentGradesListDto> getStudentGrades(Long studentId);
    Student create(Long userId, Long groupId);

}
