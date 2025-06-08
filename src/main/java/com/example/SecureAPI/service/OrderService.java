package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.CartDTO;
import com.example.SecureAPI.dto.OrderDTO;
import com.example.SecureAPI.dto.OrderItemDTO;
import com.example.SecureAPI.model.*;
import com.example.SecureAPI.repository.OrderRepository;
import com.example.SecureAPI.repository.UserRepository;
import com.example.SecureAPI.repository.ProductRepository;
import com.example.SecureAPI.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с заказами.
 * Реализует бизнес-логику создания заказов на основе данных корзины пользователя.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository; // ✅ Добавили

    /**
     * Создаёт заказ на основе содержимого корзины пользователя.
     * @param userId ID пользователя
     * @return DTO созданного заказа
     */
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

    /**
     * Получает все заказы пользователя по его ID.
     * @param userId ID пользователя
     * @return список заказов
     */
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Получает все заказы (для тестирования).
     * @return список DTO всех заказов
     */
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Конвертирует модель заказа в DTO.
     * @param order объект заказа
     * @return DTO заказа
     */
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

    /**
     * Конвертирует элемент заказа в DTO.
     * @param item элемент заказа
     * @return DTO элемента заказа
     */
    private OrderItemDTO convertItemToDTO(OrderItem item) {
        return new OrderItemDTO(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPriceAtTime()
        );
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