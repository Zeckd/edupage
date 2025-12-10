package com.example.demo.services.impl;

import com.example.demo.config.JwtUtil;
import com.example.demo.enums.Role;
import com.example.demo.models.*;
import com.example.demo.models.dtos.AuthResponseDto;
import com.example.demo.models.dtos.LoginRequestDto;
import com.example.demo.models.dtos.RegisterRequestDto;
import com.example.demo.repositories.*;
import com.example.demo.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();

        user = userRepository.save(user);

        if (dto.getRole() == Role.STUDENT) {
            if (dto.getGroupId() == null) {
                throw new RuntimeException("Group ID is required for students");
            }
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Group not found"));
            Student student = Student.builder()
                    .user(user)
                    .group(group)
                    .build();
            studentRepository.save(student);
        } else if (dto.getRole() == Role.TEACHER) {
            Teacher teacher = Teacher.builder()
                    .user(user)
                    .hireDate(dto.getHireDate() != null ? dto.getHireDate() : LocalDate.now())
                    .description(dto.getDescription())
                    .build();
            teacherRepository.save(teacher);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user.getUsername(), user.getRole(), user.getId());
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user.getUsername(), user.getRole(), user.getId());
    }
}

