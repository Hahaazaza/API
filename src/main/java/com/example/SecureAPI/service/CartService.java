package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.CartDTO;
import com.example.SecureAPI.dto.CartItemDTO;
import com.example.SecureAPI.dto.OrderDTO;
import com.example.SecureAPI.model.Cart;
import com.example.SecureAPI.model.CartItem;
import com.example.SecureAPI.model.Product;
import com.example.SecureAPI.model.User;
import com.example.SecureAPI.repository.CartRepository;
import com.example.SecureAPI.repository.ProductRepository;
import com.example.SecureAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления корзиной пользователя.
 * Реализует операции добавления товаров, получения данных и оформления заказа.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    /**
     * Получает корзину текущего пользователя и конвертирует её в DTO.
     * @param userId ID пользователя
     * @return DTO корзины
     */
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        return convertToDTO(cart);
    }

    /**
     * Получает все корзины (для тестирования).
     * @return список DTO всех корзин
     */
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Добавляет товар в корзину пользователя.
     * Если у пользователя нет корзины — создаёт новую.
     * @param userId ID пользователя
     * @param request DTO с информацией о товаре
     * @return обновлённая корзина в виде DTO
     */
    public CartDTO addToCart(Long userId, CartItemDTO request) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setCart(cart);
        cart.getItems().add(item);

        cartRepository.save(cart);

        return convertToDTO(cart);
    }

    /**
     * Очищает корзину пользователя.
     * @param userId ID пользователя
     */
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    /**
     * Создаёт заказ на основе содержимого корзины.
     * @param userId ID пользователя
     * @return DTO созданного заказа
     */
    public OrderDTO createOrderFromCart(Long userId) {
        return orderService.createOrderFromCart(userId); // ✅ Теперь работает
    }

    /**
     * Конвертирует модель корзины в DTO.
     * @param cart объект корзины
     * @return DTO корзины
     */
    private CartDTO convertToDTO(Cart cart) {
        return new CartDTO(
                cart.getId(),
                cart.getUser().getId(),
                cart.getItems().stream()
                        .map(item -> new CartItemDTO(item.getProduct().getId(), item.getQuantity()))
                        .toList());
    }

    /**
     * Конвертирует элемент корзины в DTO.
     * @param item элемент корзины
     * @return DTO элемента
     */
    private CartItemDTO convertItemToDTO(CartItem item) {
        return new CartItemDTO(item.getProduct().getId(), item.getQuantity());
    }
}