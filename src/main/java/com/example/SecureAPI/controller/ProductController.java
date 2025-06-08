package com.example.SecureAPI.controller;

import com.example.SecureAPI.model.Product;
import com.example.SecureAPI.dto.ProductDTO;
import com.example.SecureAPI.service.ProductService;
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
    @PostMapping("/public-add")
    public ResponseEntity<Product> publicAddProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}