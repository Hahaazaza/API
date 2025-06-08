package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTO для элемента заказа.
 * Содержит ID продукта, количество и цену на момент оформления заказа.
 */
@Data
@AllArgsConstructor
@Schema(description = "Элемент заказа")
public class OrderItemDTO {
    @Schema(description = "ID продукта", example = "1001")
    private Long productId;

    @Schema(description = "Количество товара", example = "2")
    private Integer quantity;

    @Schema(description = "Цена на момент оформления", example = "499.99")
    private BigDecimal priceAtTime;
}