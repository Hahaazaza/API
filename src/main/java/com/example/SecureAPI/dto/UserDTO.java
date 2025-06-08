package com.example.SecureAPI.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTO для передачи информации о пользователе.
 * Содержит ID, email, имя и роль пользователя.
 */
@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
}