package com.example.SecureAPI.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}