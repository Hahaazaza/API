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

/**
 * Контроллер, отвечающий за аутентификацию: регистрация, вход, просмотр списка пользователей.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Конструктор для внедрения зависимости UserService.
     * @param userService сервис для работы с пользователями
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация нового пользователя.
     * @param request данные для регистрации (email, пароль, имя)
     * @return ответ с сообщением о регистрации и статусом CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(CREATED).body("User registered");
    }

    /**
     * Аутентификация пользователя (логин).
     * @param request содержит email и пароль
     * @return JWT токен и данные пользователя в виде AuthResponse
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Получение списка всех пользователей (доступно только администраторам).
     * @return список пользователей
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Публичный эндпоинт для получения информации о пользователях (для тестирования).
     * Преобразует User в UserDTO для безопасной передачи данных без exposing внутренних деталей.
     * @return список DTO объектов пользователей
     */
    @GetMapping("/users/public")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<UserDTO>> getAllUsersPublic() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getRole()))
                .toList();
        return ResponseEntity.ok(users);
    }
}