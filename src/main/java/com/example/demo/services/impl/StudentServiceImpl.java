package com.example.demo.services.impl;

import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.mappers.GradeMapper;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.*;
import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.models.dtos.StudentAttendanceReportDto;
import com.example.demo.models.dtos.StudentGradesListDto;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.repositories.*;
import com.example.demo.services.AttendanceService;
import com.example.demo.services.GradeService;
import com.example.demo.services.ScheduleService;
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
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AttendanceService attendanceService;
    private final GradeService gradeService;
    private final ScheduleService scheduleService;

    private final StudentMapper studentMapper = StudentMapper.INSTANCE;
    private final AttendanceMapper attendanceMapper = AttendanceMapper.INSTANCE;
    private final GradeMapper gradeMapper = GradeMapper.INSTANCE;

    @Override
    public List<StudentStatusDto> getStudentsByGroup(Long groupId) {
        return studentRepository.findByGroupId(groupId)
                .stream()
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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        studentRepository.findByUserId(userId).ifPresent(s -> {
            throw new RuntimeException("Student with this user already exists");
        });

        Student student = Student.builder().user(user).group(group).build();
        return studentRepository.save(student);
    }

    public Student getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public List<?> getAttendance(String username) {
        Student student = getProfile(username);
        return attendanceService.getStudentAttendance(student.getId());
    }

    public List<?> getGrades(String username) {
        Student student = getProfile(username);
        return gradeService.getStudentGrades(student.getId());
    }

    public List<ScheduleEntryDto> getSchedule(String username) {
        Student student = getProfile(username);
        return scheduleService.getScheduleForGroup(student.getGroup().getId());
    }
}


