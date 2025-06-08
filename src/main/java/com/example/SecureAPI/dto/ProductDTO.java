package com.example.SecureAPI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO для создания/обновления продукта.
 * Содержит название и цену продукта с валидацией.
 */
@Data
@Schema(description = "Данные для создания или обновления продукта")
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    @Schema(description = "Название продукта", example = "Laptop")
    private String name;

    @Positive(message = "Price must be greater than zero")
    @Schema(description = "Цена продукта", example = "999.99")
    private double price;
}

/**
 *             _____                                      _____
 *           ,888888b.                                  ,888888b.
 *         .d888888888b                               .d888888888b
 *     _..-'.`*'_,88888b                          _..-'.`*'_,88888b
 *   ,'..-..`"ad88888888b.                      ,'..-..`"ad88888888b.
 *          ``-. `*Y888888b.                           ``-. `*Y888888b.
 *              \   `Y888888b.                             \   `Y888888b.
 *              :     Y8888888b.                           :     Y8888888b.
 *              :      Y88888888b.                         :      Y88888888b.
 *              |    _,8ad88888888.                        |    _,8ad88888888.
 *              : .d88888888888888b.                       : .d88888888888888b.
 *              \d888888888888888888                       \d888888888888888888
 *              8888;'''`88888888888                       8888;ss'`88888888888
 *              888'     Y8888888888                       888'N""N Y8888888888
 *              `Y8      :8888888888                       `Y8 N  " :8888888888
 *               |`      '8888888888                        |` N    '8888888888
 *               |        8888888888                        |  N     8888888888
 *               |        8888888888                        |  N     8888888888
 *               |        8888888888                        |  N     8888888888
 *               |       ,888888888P                        |  N    ,888888888P
 *               :       ;888888888'                        :  N    ;888888888'
 *                \      d88888888'                         :  N    ;888888888'
 *               _.>,    888888P'                            \ N    d88888888'
 *             <,--''`.._>8888(                             _.>N    888888P'
 *              `>__...--' `''` SSt                       <,--'N`.._>8888(
 *                                                         `>__N..--' `''` SSt
 *                                                             N
 */