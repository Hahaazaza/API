package com.example.SecureAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO для создания/обновления продукта.
 * Содержит название и цену продукта с валидацией.
 */
@Data
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Price must be greater than zero")
    private double price;
}