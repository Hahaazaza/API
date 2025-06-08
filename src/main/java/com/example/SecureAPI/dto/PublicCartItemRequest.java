package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для тестового добавления товара в корзину гостя.
 * Использует record-синтаксис (Java 16+).
 */
@Schema(description = "Запрос на добавление товара в гостевую корзину")
public record PublicCartItemRequest(
        @Schema(description = "ID продукта", example = "1001") Long productId,
        @Schema(description = "Количество", example = "2") Integer quantity) {}