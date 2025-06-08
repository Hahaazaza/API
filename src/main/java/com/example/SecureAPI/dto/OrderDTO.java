package com.example.SecureAPI.dto;

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
public class OrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}