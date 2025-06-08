package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.CartDTO;
import com.example.SecureAPI.dto.OrderDTO;
import com.example.SecureAPI.dto.OrderItemDTO;
import com.example.SecureAPI.model.*;
import com.example.SecureAPI.repository.CartRepository;
import com.example.SecureAPI.repository.OrderRepository;
import com.example.SecureAPI.repository.ProductRepository;
import com.example.SecureAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository; // ✅ Добавили

    @Transactional
    public OrderDTO createOrderFromCart(Long userId) {
        // Получаем корзину пользователя
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        User user = userRepository.findById(userId).orElseThrow();

        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        order = orderRepository.save(order);

        // 🔁 Сохраняем order один раз до использования в лямбде
        Order savedOrder = order;

        List<OrderItem> items = cart.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(item.getProduct());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPriceAtTime(BigDecimal.valueOf(item.getProduct().getPrice()));
                    orderItem.setOrder(savedOrder); // ✅ Теперь можно использовать
                    return orderItem;
                })
                .toList();

        order.setItems(items);
        orderRepository.save(order);

        // Очищаем корзину
        cart.getItems().clear();
        cartRepository.save(cart);

        return convertToDTO(order);
    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPriceAtTime()
                        ))
                        .toList()
        );
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        return new OrderItemDTO(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPriceAtTime()
        );
    }
}