package com.example.SecureAPI.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTO для элемента заказа.
 * Содержит ID продукта, количество и цену на момент оформления заказа.
 */
@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
    private BigDecimal priceAtTime;
}