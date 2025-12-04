package com.example.demo.models.dtos;

import java.util.Date;

public record StudentCreateDto (
        String firstName,
        String lastName,
        String patronymic,
        Date dateOfBirth,
        String address

        ) {

}
