package com.example.demo.services.impl;

import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.mappers.GradeMapper;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Group;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentGradesListDto;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.repositories.*;
import com.example.demo.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;

    private final StudentMapper studentMapper;
    private final AttendanceMapper attendanceMapper;
    private final GradeMapper gradeMapper;

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public List<StudentStatusDto> getStudentsByGroup(Long groupId) {
        List<Student> students = studentRepository.findByGroupId(groupId);

        return students.stream()
                .map(studentMapper::toStudentStatusDto)
                .toList();
    }

    @Override
    public List<StudentAttendanceReportDto> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId)
                .stream()
                .map(attendanceMapper::toStudentAttendanceReportDto)
                .toList();
    }

    @Override
    public List<StudentGradesListDto> getStudentGrades(Long studentId) {
        return gradeRepository.findByStudentId(studentId)
                .stream()
                .map(gradeMapper::toStudentGradesListDto)
                .toList();
    }

    @Override
    @Transactional
    public Student create(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if user already has student record
        studentRepository.findByUserId(userId).ifPresent(s -> {
            throw new RuntimeException("Student with this user already exists");
        });

        Student student = Student.builder()
                .user(user)
                .group(group)
                .build();

        return studentRepository.save(student);
    }
}
