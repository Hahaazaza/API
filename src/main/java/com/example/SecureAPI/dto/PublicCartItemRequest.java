package com.example.SecureAPI.dto;

/**
 * DTO для тестового добавления товара в корзину гостя.
 * Использует record-синтаксис (Java 16+).
 */
public record PublicCartItemRequest(Long productId, Integer quantity) {}