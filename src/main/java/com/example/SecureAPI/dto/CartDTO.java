package com.example.SecureAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * DTO для передачи данных о корзине пользователя.
 * Включает ID корзины, ID пользователя и список товаров в корзине.
 */
@Data
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
}