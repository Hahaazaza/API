package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для запроса аутентификации (логина).
 * Содержит email и пароль пользователя.
 */
@Data
@Schema(description = "Запрос на аутентификацию пользователя")
public class AuthRequest {
    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;
}