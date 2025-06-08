package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.AuthRequest;
import com.example.SecureAPI.dto.AuthResponse;
import com.example.SecureAPI.dto.RegisterRequest;
import com.example.SecureAPI.dto.UserDTO;
import com.example.SecureAPI.model.User;
import com.example.SecureAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(CREATED).body("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Просто для теста

    @GetMapping("/users/public")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<UserDTO>> getAllUsersPublic() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getRole()))
                .toList();
        return ResponseEntity.ok(users);
    }
}