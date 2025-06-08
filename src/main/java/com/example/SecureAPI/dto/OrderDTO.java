package com.example.SecureAPI.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}