package com.example.SecureAPI.model;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String passwordHash;
    private String name; // Разбить name на ФИО (замечание для будущего расширения)
    private String role; // ADMIN, EMPLOYEE, CLIENT

    // Связь с корзиной (OneToOne)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Cart cart;
}