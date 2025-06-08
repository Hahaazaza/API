package com.example.SecureAPI.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
    private BigDecimal priceAtTime;
}