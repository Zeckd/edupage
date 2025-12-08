package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.dtos.UserDto;

public interface UserService {
    User create(UserDto dto);
    User findById(Long id);
}
