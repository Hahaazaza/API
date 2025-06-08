package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTO для передачи информации о пользователе.
 * Содержит ID, email, имя и роль пользователя.
 */
@Data
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserDTO {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Полное имя пользователя", example = "John Doe")
    private String name;

    @Schema(description = "Роль пользователя", example = "CLIENT")
    private String role;
}