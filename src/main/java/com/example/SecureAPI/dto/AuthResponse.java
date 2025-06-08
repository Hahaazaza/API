package com.example.SecureAPI.dto;

import lombok.Data;

/**
 * DTO для ответа после успешной аутентификации.
 * Содержит JWT токен, ID пользователя и его роль.
 */
@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String role;

    public AuthResponse(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }
}