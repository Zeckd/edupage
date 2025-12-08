package com.example.demo.services.impl;

import com.example.demo.enums.AttendanceStatus;
import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.models.dtos.AttendanceRequestDto;
import com.example.demo.models.dtos.LessonAttendanceDto;
import com.example.demo.repositories.AttendanceRepository;
import com.example.demo.repositories.LessonRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper mapper;

    @Override
    public void setAttendance(AttendanceRequestDto dto) {
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        dto.getStudents().forEach(st -> {
            Student student = studentRepository.findById(st.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Attendance attendance = attendanceRepository
                    .findByLessonIdAndStudentId(lesson.getId(), student.getId())
                    .orElseGet(() -> attendanceRepository.save(
                            Attendance.builder()
                                    .lesson(lesson)
                                    .student(student)
                                    .status(AttendanceStatus.PRESENT)
                                    .build()
                    ));


            attendance.setStatus(st.getStatus());
            attendance.setComment(st.getComment());
            attendanceRepository.save(attendance);
        });
    }

    @Override
    public LessonAttendanceDto getLessonAttendance(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<Attendance> attendances = attendanceRepository.findByLessonId(lessonId);

        LessonAttendanceDto dto =
                mapper.toLessonAttendanceDto(attendances.get(0));

        dto.setStudents(mapper.toStudentStatusList(attendances));
        return dto;
    }

    @Override
    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
