package com.aml.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private String sex;
    private String birthdate; // keep as String, then parse to LocalDate in controller
}
