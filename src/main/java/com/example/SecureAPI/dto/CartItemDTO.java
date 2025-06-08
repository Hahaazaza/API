package com.example.SecureAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для элемента корзины.
 * Содержит ID продукта и количество.
 */
@Data
@AllArgsConstructor
public class CartItemDTO {
    private Long productId;
    private Integer quantity;
}