package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.ProductDTO;
import com.example.SecureAPI.model.Product;
import java.util.List;

/**
 * Интерфейс сервиса для работы с продуктами.
 */
public interface ProductService {
    List<Product> getAllProducts();
    Product createProduct(ProductDTO dto);
}