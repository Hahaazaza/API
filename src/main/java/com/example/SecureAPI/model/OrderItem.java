package com.example.SecureAPI.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

/**
 * Сущность элемента заказа.
 * Представляет один товар в заказе с ценой на момент оформления.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@Schema(description = "Элемент заказа с информацией о товаре и цене на момент покупки")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID элемента заказа", example = "301")
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "Заказ, к которому относится этот элемент")
    private Order order;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "Продукт, вошедший в заказ")
    private Product product;

    @Column(nullable = false)
    @Schema(description = "Количество товара в заказе", example = "2")
    private Integer quantity;

    @Column(name = "price_at_time", nullable = false)
    @Schema(description = "Цена товара на момент оформления", example = "499.99")
    private BigDecimal priceAtTime;
}