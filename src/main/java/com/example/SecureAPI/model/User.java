package com.example.SecureAPI.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность пользователя.
 * Содержит данные о пользователе: email, хэш пароля, имя, роль (ADMIN/EMPLOYEE/CLIENT).
 * Имеет связь с корзиной (One-to-One).
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Schema(description = "Информация о пользователе системы")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Хэшированный пароль", example = "$2a$10$abc123xyz...")
    private String passwordHash;

    @Schema(description = "Полное имя пользователя", example = "John Doe")
    private String name; // Разбить name на ФИО (замечание для будущего расширения)

    @Schema(description = "Роль пользователя", example = "CLIENT")
    private String role; // ADMIN, EMPLOYEE, CLIENT

    // Связь с корзиной (OneToOne)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Schema(description = "Корзина пользователя")
    private Cart cart;
}