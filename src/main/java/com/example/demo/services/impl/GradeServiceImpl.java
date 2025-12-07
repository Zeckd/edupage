package com.example.demo.services.impl;

import com.example.demo.mappers.GradeAttendanceMapper;
import com.example.demo.models.*;
import com.example.demo.models.dtos.*;
import com.example.demo.repositories.*;
import com.example.demo.services.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final GradeAttendanceMapper mapper;

    @Override
    public Grade addGrade(GradeRequestDto dto) {

        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Grade grade = Grade.builder()
                .lesson(lesson)
                .student(student)
                .value(dto.getValue())
                .comment(dto.getComment())
                .type(dto.getType())
                .createdAt(LocalDateTime.now())
                .build();

        return gradeRepository.save(grade);
    }

    @Override
    public List<StudentGradesListDto> getStudentGrades(Long studentId) {
        return gradeRepository.findByStudentId(studentId)
                .stream()
                .map(mapper::toStudentGradesListDto)
                .collect(Collectors.toList());
    }
}
