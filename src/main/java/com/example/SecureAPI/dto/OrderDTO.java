package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для заказа.
 * Содержит ID заказа, ID пользователя, общую сумму, дату заказа и список товаров.
 */
@Data
@AllArgsConstructor
@Schema(description = "Информация о заказе")
public class OrderDTO {
    @Schema(description = "ID заказа", example = "5001")
    private Long id;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Общая сумма заказа", example = "999.99")
    private BigDecimal totalPrice;

    @Schema(description = "Дата и время оформления заказа", example = "2025-04-05T12:34:56")
    private LocalDateTime orderDate;

    @Schema(description = "Список товаров в заказе")
    private List<OrderItemDTO> items;
}