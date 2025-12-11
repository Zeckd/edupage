package com.example.demo.services.impl;

import com.example.demo.mappers.TeacherMapper;
import com.example.demo.models.Teacher;
import com.example.demo.models.User;
import com.example.demo.models.dtos.*;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.LessonService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.ScheduleService;
import com.example.demo.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {


    private final LessonService lessonService;
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final NotificationService notificationService;
    private final TeacherMapper teacherMapper = TeacherMapper.INSTANCE;

    @Override
    public TeacherDto create(TeacherCreateDto dto) {
        Teacher teacher = teacherMapper.createDtoToTeacher(dto);
        teacherRepository.save(teacher);
        return teacherMapper.teacherToDto(teacher);
    }

    @Override
    public List<TeacherDto> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::teacherToDto)
                .toList();
    }

    @Override
    public TeacherDto getByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return teacherMapper.teacherToDto(teacher);
    }

    @Override
    public Teacher getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    @Override
    public List<LessonDto> getLessons(String username) {
        Teacher teacher = getProfile(username);
        return lessonService.findByTeacher(teacher.getId());
    }

    @Override
    public LessonDto createLesson(String username, LessonCreateDto dto) {
        Teacher teacher = getProfile(username);
        dto.setTeacherId(teacher.getId());
        LessonDto lesson = lessonService.create(dto);

        notificationService.sendNotificationToRole(
                "STUDENT",
                "Новый урок назначен: " + lesson.getSubjectName()
        );

        return lesson;
    }

    @Override
    public List<ScheduleEntryDto> getSchedule(String username) {
        Teacher teacher = getProfile(username);
        return scheduleService.getScheduleForTeacher(teacher.getId());
    }
}
