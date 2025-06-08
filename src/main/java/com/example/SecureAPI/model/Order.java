package com.example.SecureAPI.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность заказа.
 * Представляет собой оформленный заказ пользователя с датой, общей суммой и списком товаров.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Schema(description = "Информация о заказе пользователя")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID заказа", example = "5001")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь, оформивший заказ")
    private User user;

    @Column(name = "total_price", nullable = false)
    @Schema(description = "Общая сумма заказа", example = "999.99")
    private BigDecimal totalPrice;

    @Column(name = "order_date", nullable = false)
    @Schema(description = "Дата и время оформления заказа", example = "2025-04-05T12:34:56")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список товаров в заказе")
    private List<OrderItem> items = new ArrayList<>();
}