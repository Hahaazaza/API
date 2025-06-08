package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.CartDTO;
import com.example.SecureAPI.dto.CartItemDTO;
import com.example.SecureAPI.dto.PublicCartItemRequest;
import com.example.SecureAPI.model.Cart;
import com.example.SecureAPI.model.User;
import com.example.SecureAPI.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody CartItemDTO request) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        cartService.createOrderFromCart(userId);
        return ResponseEntity.ok("Order created successfully");
    }


    //Просто для теста

    @GetMapping("/all")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<CartDTO>> getAllCartsPublic() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @PostMapping("/public-add")
    @PreAuthorize("permitAll")
    public ResponseEntity<CartDTO> publicAddToCart(@RequestBody PublicCartItemRequest request) {
        Long userId = 1L; // Можно заменить на логику поиска гостевой корзины
        CartItemDTO itemDTO = new CartItemDTO(request.productId(), request.quantity());
        return ResponseEntity.ok(cartService.addToCart(userId, itemDTO));
    }
}