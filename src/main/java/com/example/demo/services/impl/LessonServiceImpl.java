package com.example.demo.services.impl;

import com.example.demo.enums.AttendanceStatus;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.models.*;
import com.example.demo.models.dtos.LessonCreateDto;
import com.example.demo.models.dtos.LessonDto;
import com.example.demo.repositories.*;
import com.example.demo.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ScheduleEntryRepository scheduleEntryRepository;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public LessonDto create(LessonCreateDto dto) {

        Lesson lesson = LessonMapper.INSTANCE.lessonCreateDtoToLesson(dto);

        lesson.setScheduleEntry(scheduleEntryRepository.findById(dto.getScheduleEntryId()).orElseThrow());
        lesson.setGroup(groupRepository.findById(dto.getGroupId()).orElseThrow());
        lesson.setSubject(subjectRepository.findById(dto.getSubjectId()).orElseThrow());
        lesson.setTeacher(teacherRepository.findById(dto.getTeacherId()).orElseThrow());

        final Lesson savedLesson = lessonRepository.save(lesson);

        List<Student> students = studentRepository.findByGroupId(dto.getGroupId());

        students.forEach(st -> {
            Attendance att = Attendance.builder()
                    .lesson(savedLesson)
                    .student(st)
                    .status(AttendanceStatus.PRESENT)
                    .build();
            attendanceRepository.save(att);
        });

        return LessonMapper.INSTANCE.lessonToLessonDto(savedLesson);

    }

    @Override
    public LessonDto findById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow();
        return LessonMapper.INSTANCE.lessonToLessonDto(lesson);
    }

    @Override
    public List<LessonDto> findByGroup(Long groupId) {
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);

        return lessonRepository.findByGroup_IdAndLessonDateTimeBetween(groupId, start, end)
                .stream()
                .map(LessonMapper.INSTANCE::lessonToLessonDto)
                .toList();
    }

    @Override
    public List<LessonDto> findByTeacher(Long teacherId) {

        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);

        return lessonRepository.findByTeacher_IdAndLessonDateTimeBetween(teacherId, start, end)
                .stream()
                .map(LessonMapper.INSTANCE::lessonToLessonDto)
                .toList();
    }
}
