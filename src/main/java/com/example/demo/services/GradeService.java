package com.example.demo.services;

import com.example.demo.models.Grade;
import com.example.demo.models.dtos.GradeRequestDto;
import com.example.demo.models.dtos.StudentGradesListDto;

import java.util.List;

public interface GradeService {

    Grade addGrade(GradeRequestDto dto);

    List<StudentGradesListDto> getStudentGrades(Long studentId);
}
