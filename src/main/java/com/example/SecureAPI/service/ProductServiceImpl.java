package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.ProductDTO;
import com.example.SecureAPI.exception.InvalidProductDataException;
import com.example.SecureAPI.model.Product;
import com.example.SecureAPI.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса продуктов.
 * Предоставляет методы для получения и добавления товаров.
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * Возвращает список всех доступных продуктов.
     * @return список продуктов
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Создаёт новый продукт на основе DTO.
     * @param dto данные нового продукта
     * @return сохранённый продукт
     */
    @Override
    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return productRepository.save(product);
    }
}