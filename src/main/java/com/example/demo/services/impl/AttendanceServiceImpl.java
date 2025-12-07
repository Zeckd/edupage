package com.example.demo.services.impl;

import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.models.dtos.AttendanceRequestDto;
import com.example.demo.models.dtos.LessonAttendanceDto;
import com.example.demo.models.dtos.StudentStatusDto;
import com.example.demo.repositories.AttendanceRepository;
import com.example.demo.repositories.LessonRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Override
    public void saveAttendance(AttendanceRequestDto dto) {
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        for (AttendanceRequestDto.StudentAttendanceDto st : dto.getStudents()) {

            Student student = studentRepository.findById(st.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Attendance attendance = attendanceRepository
                    .findByLessonIdAndStudentId(lesson.getId(), student.getId())
                    .orElse(Attendance.builder()
                            .lesson(lesson)
                            .student(student)
                            .build());

            attendance.setStatus(st.getStatus());
            attendance.setComment(st.getComment());

            attendanceRepository.save(attendance);
        }
    }

    @Override
    public LessonAttendanceDto getLessonAttendance(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<Attendance> attendances = attendanceRepository.findByLessonId(lessonId);

        LessonAttendanceDto dto = new LessonAttendanceDto();
        dto.setLessonId(lessonId);
        dto.setSubjectName(lesson.getSubject().getName());
        dto.setGroupName(lesson.getGroup().getName());
        dto.setLessonDateTime(lesson.getLessonDateTime());

        dto.setStudents(attendances.stream().map(a -> {
            StudentStatusDto s = new StudentStatusDto();
            s.setStudentId(a.getStudent().getId());
            s.setStudentFullName(a.getStudent().getUser().getFirstName() + " " +
                    a.getStudent().getUser().getLastName());
            s.setStatus(a.getStatus());
            s.setComment(a.getComment());
            return s;
        }).collect(Collectors.toList()));

        return dto;
    }

    @Override
    public List<Attendance> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
