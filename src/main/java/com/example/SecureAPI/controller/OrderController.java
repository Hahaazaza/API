package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.OrderDTO;
import com.example.SecureAPI.model.Order;
import com.example.SecureAPI.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с заказами.
 * Доступ разрешён только клиентам, сотрудникам и администраторам.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
public class OrderController {

    private final OrderService orderService;

    /**
     * Получить список заказов текущего пользователя.
     * @param userDetails данные аутентифицированного пользователя
     * @return список DTO заказов пользователя
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((com.example.SecureAPI.model.User) userDetails).getId();
        List<OrderDTO> dtos = orderService.getAllOrdersByUserId(userId)
                .stream()
                .map(orderService::convertToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Публичный тестовый эндпоинт для получения всех заказов (для тестирования).
     * @return список всех заказов в виде DTO
     */
    @GetMapping("/all")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Тестовый эндпоинт для создания заказа от имени гостя.
     * @return DTO созданного заказа
     */
    @PostMapping("/public-create")
    @PreAuthorize("permitAll")
    public ResponseEntity<OrderDTO> publicCreateOrder() {
        Long userId = 1L; // Можно использовать гостевого пользователя
        return ResponseEntity.ok(orderService.createOrderFromCart(userId));
    }
}