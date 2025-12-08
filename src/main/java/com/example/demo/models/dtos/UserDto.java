package com.example.demo.models.dtos;

import com.example.demo.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}