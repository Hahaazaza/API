package com.example.SecureAPI.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность корзины пользователя.
 * Хранит список товаров, которые пользователь собирается купить.
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@Schema(description = "Модель корзины пользователя")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID корзины", example = "101")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Пользователь, которому принадлежит корзина")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список товаров в корзине")
    private List<CartItem> items = new ArrayList<>();
}