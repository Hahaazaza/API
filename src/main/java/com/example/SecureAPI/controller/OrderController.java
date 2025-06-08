package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.OrderDTO;
import com.example.SecureAPI.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "Получить список заказов текущего пользователя",
            description = "Возвращает список всех заказов авторизованного пользователя.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заказов", content = @Content(schema = @Schema(implementation = OrderDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Заказы не найдены")
            }
    )
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
    @Operation(
            summary = "Получить все заказы (тестовый)",
            description = "Тестовый эндпоинт для получения списка всех заказов в системе.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех заказов", content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            }
    )
    @GetMapping("/all")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Тестовый эндпоинт для создания заказа от имени гостя.
     * @return DTO созданного заказа
     */
    @Operation(
            summary = "Создать заказ от имени гостя (тестовый)",
            description = "Тестовая функция для создания заказа из корзины гостевого пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ успешно создан", content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            }
    )
    @PostMapping("/public-create")
    @PreAuthorize("permitAll")
    public ResponseEntity<OrderDTO> publicCreateOrder() {
        Long userId = 1L; // Можно использовать гостевого пользователя
        return ResponseEntity.ok(orderService.createOrderFromCart(userId));
    }
}