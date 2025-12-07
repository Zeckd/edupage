package com.example.demo.controllers;

import com.example.demo.models.Student;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/group/{groupId}")
    public List<Student> getStudentsByGroup(@PathVariable Long groupId) {
        return studentService.getStudentsByGroup(groupId);
    }

    @GetMapping("/{id}/attendance")
    public List<StudentStatusDto> getStudentAttendance(@PathVariable Long id) {
        return studentService.getStudentAttendance(id);
    }
}
