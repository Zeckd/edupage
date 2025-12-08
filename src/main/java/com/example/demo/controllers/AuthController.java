package com.example.demo.controllers;

import com.example.demo.models.dtos.UserDto;
//import com.example.demo.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

//    private final AuthService authService;

//    @PostMapping("/register")
//    public UserDto register(@RequestBody UserDto dto) {
//        return authService.register(dto);
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        return authService.login(username, password); // Возвращает JWT
//    }
}
