package com.example.demo.controllers;

import com.example.demo.models.dtos.GradeRequestDto;
import com.example.demo.models.dtos.StudentGradesListDto;
import com.example.demo.services.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("/add")
    public void addGrade(@RequestBody GradeRequestDto dto) {
        gradeService.addGrade(dto);
    }

    @GetMapping("/student/{studentId}")
    public List<StudentGradesListDto> getStudentGrades(@PathVariable Long studentId) {
        return gradeService.getStudentGrades(studentId);
    }
}
