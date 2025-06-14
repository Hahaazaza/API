package com.example.SecureAPI.controller;

import com.example.SecureAPI.model.Product;
import com.example.SecureAPI.dto.ProductDTO;
import com.example.SecureAPI.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления продуктами.
 * Только сотрудники и администраторы могут управлять продуктами.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Получить список всех продуктов.
     * @return список продуктов
     */
    @Operation(
            summary = "Получить список всех продуктов",
            description = "Возвращает список всех продуктов в системе.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список продуктов", content = @Content(schema = @Schema(implementation = Product.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(products);
    }

    /**
     * Создать новый продукт.
     * @param dto DTO нового продукта
     * @return созданный продукт и статус CREATED
     */
    @Operation(
            summary = "Создать новый продукт",
            description = "Добавляет новый товар в систему.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Продукт успешно создан", content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Публичный тестовый эндпоинт для получения всех продуктов.
     * @return список всех продуктов
     */
    @Operation(
            summary = "Получить все продукты (публичный)",
            description = "Тестовый эндпоинт для получения списка всех продуктов в системе.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список продуктов", content = @Content(schema = @Schema(implementation = Product.class)))
            }
    )
    @GetMapping("/public")
    public ResponseEntity<List<Product>> getAllProductsPublic() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(products);
    }

    /**
     * Тестовый эндпоинт для публичного добавления продукта.
     * @param dto DTO нового продукта
     * @return созданный продукт и статус CREATED
     */
    @Operation(
            summary = "Добавить продукт (тестовый)",
            description = "Тестовый эндпоинт для добавления продукта без аутентификации.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Продукт успешно создан", content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    @PostMapping("/public-add")
    public ResponseEntity<Product> publicAddProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
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