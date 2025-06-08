package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.AuthRequest;
import com.example.SecureAPI.dto.AuthResponse;
import com.example.SecureAPI.dto.RegisterRequest;
import com.example.SecureAPI.dto.UserDTO;
import com.example.SecureAPI.model.User;
import com.example.SecureAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param request данные для регистрации (email, пароль, имя)
     * @return ответ с сообщением о регистрации и статусом CREATED
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя в системе по предоставленным данным.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Невалидные данные запроса")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(CREATED).body("User registered");
    }

    /**
     * Аутентификация пользователя (логин).
     *
     * @param request содержит email и пароль
     * @return JWT токен и данные пользователя в виде AuthResponse
     */
    @Operation(
            summary = "Вход пользователя в систему",
            description = "Производит аутентификацию пользователя и возвращает JWT-токен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Получение списка всех пользователей (доступно только администраторам).
     *
     * @return список пользователей
     */
    @Operation(
            summary = "Получить список всех пользователей",
            description = "Доступно только администраторам. Возвращает список всех зарегистрированных пользователей.",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
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
    @Operation(
            summary = "Получить список всех пользователей (публичный)",
            description = "Тестовый эндпоинт. Возвращает список пользователей в формате DTO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(schema = @Schema(implementation = UserDTO.class)))
            }
    )
    @GetMapping("/users/public")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<UserDTO>> getAllUsersPublic() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getRole()))
                .toList();
        return ResponseEntity.ok(users);
    }
}