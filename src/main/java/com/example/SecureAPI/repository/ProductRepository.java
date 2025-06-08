package com.example.SecureAPI.repository;

import com.example.SecureAPI.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Репозиторий для работы с продуктами.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
}