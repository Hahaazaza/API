package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для регистрации нового пользователя.
 * Содержит email, пароль, имя и роль (ADMIN, EMPLOYEE, CLIENT).
 */
@Data
@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterRequest {
    @Schema(description = "Email пользователя", example = "newuser@example.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @Schema(description = "Полное имя пользователя", example = "John Doe")
    private String name;

    @Schema(description = "Роль пользователя", example = "CLIENT")
    private String role; // ADMIN, EMPLOYEE, CLIENT
}