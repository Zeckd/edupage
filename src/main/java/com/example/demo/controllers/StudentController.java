package com.example.demo.controllers;

import com.example.demo.models.Student;
import com.example.demo.models.dtos.StudentCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    public ResponseEntity<?> createStudent(StudentCreateDto studentCreateDto) {
        return null;
    }
}
