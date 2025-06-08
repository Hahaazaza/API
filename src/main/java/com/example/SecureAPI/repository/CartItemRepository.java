package com.example.SecureAPI.repository;

import com.example.SecureAPI.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Репозиторий для работы с элементами корзины.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
}