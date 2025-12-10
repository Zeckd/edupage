package com.example.demo.services;

import com.example.demo.models.dtos.AuthResponseDto;
import com.example.demo.models.dtos.LoginRequestDto;
import com.example.demo.models.dtos.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);
}

