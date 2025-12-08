package com.example.demo.services;

import com.example.demo.models.Attendance;
import com.example.demo.models.dtos.AttendanceRequestDto;
import com.example.demo.models.dtos.LessonAttendanceDto;

import java.util.List;

public interface AttendanceService {

    void setAttendance(AttendanceRequestDto dto);

    LessonAttendanceDto getLessonAttendance(Long lessonId);

    List<Attendance> getStudentAttendance(Long studentId);
}
