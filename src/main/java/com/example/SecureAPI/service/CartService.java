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

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        return convertToDTO(cart);
    }

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

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public OrderDTO createOrderFromCart(Long userId) {
        return orderService.createOrderFromCart(userId); // ✅ Теперь работает
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(cart.getItems().stream()
                .map(this::convertItemToDTO)
                .toList());
        return dto;
    }

    private CartItemDTO convertItemToDTO(CartItem item) {
        return new CartItemDTO(item.getProduct().getId(), item.getQuantity());
    }
}