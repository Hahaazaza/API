package com.example.SecureAPI.dto;

import lombok.Data;

/**
 * DTO для запроса аутентификации (логина).
 * Содержит email и пароль пользователя.
 */
@Data
public class AuthRequest {
    private String email;
    private String password;
}