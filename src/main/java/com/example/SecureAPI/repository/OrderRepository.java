package com.example.SecureAPI.repository;

import com.example.SecureAPI.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Репозиторий для работы с заказами.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}