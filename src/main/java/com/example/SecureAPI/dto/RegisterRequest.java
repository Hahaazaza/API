package com.example.SecureAPI.dto;

import lombok.Data;

/**
 * DTO для регистрации нового пользователя.
 * Содержит email, пароль, имя и роль (ADMIN, EMPLOYEE, CLIENT).
 */
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String role; // ADMIN, EMPLOYEE, CLIENT
}