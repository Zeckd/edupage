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
    @PostMapping("/create")
    public Student create(@RequestParam Long userId, @RequestParam Long groupId) {
        return studentService.create(userId, groupId);
    }

    @GetMapping("/group/{groupId}")
    public List<StudentStatusDto> getStudentsByGroup(@PathVariable Long groupId) {
        return studentService.getStudentsByGroup(groupId);
    }

    @GetMapping("/{id}/attendance")
    public List<?> getAttendance(@PathVariable Long id) {
        return studentService.getStudentAttendance(id);
    }

    @GetMapping("/{id}/grades")
    public List<?> getGrades(@PathVariable Long id) {
        return studentService.getStudentGrades(id);
    }
}
