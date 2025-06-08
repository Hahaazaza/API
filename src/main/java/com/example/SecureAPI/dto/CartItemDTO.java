package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для элемента корзины.
 * Содержит ID продукта и количество.
 */
@Data
@AllArgsConstructor
@Schema(description = "Элемент корзины")
public class CartItemDTO {
    @Schema(description = "ID продукта", example = "1001")
    private Long productId;

    @Schema(description = "Количество товара", example = "2")
    private Integer quantity;
}