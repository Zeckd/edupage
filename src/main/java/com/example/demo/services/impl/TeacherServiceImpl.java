package com.example.demo.services.impl;

import com.example.demo.mappers.TeacherMapper;
import com.example.demo.models.Teacher;
import com.example.demo.models.User;
import com.example.demo.models.dtos.TeacherCreateDto;
import com.example.demo.models.dtos.TeacherDto;
import com.example.demo.repositories.TeacherRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Override
    public TeacherDto create(TeacherCreateDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Teacher teacher = TeacherMapper.INSTANCE.createDtoToTeacher(dto);
        teacher.setHireDate(LocalDate.now());
        teacher.setUser(user);

        teacherRepository.save(teacher);
        return TeacherMapper.INSTANCE.teacherToDto(teacher);
    }

    @Override
    public List<TeacherDto> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper.INSTANCE::teacherToDto)
                .toList();
    }

    @Override
    public TeacherDto getByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return TeacherMapper.INSTANCE.teacherToDto(teacher);
    }
}
