package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для ответа после успешной аутентификации.
 * Содержит JWT токен, ID пользователя и его роль.
 */
@Data
@Schema(description = "Ответ после успешной аутентификации")
public class AuthResponse {
    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx")
    private String token;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Роль пользователя", example = "CLIENT")
    private String role;

    public AuthResponse(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }
}