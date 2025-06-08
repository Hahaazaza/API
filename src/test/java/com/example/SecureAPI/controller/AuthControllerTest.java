package com.example.SecureAPI.controller;

import com.example.SecureAPI.dto.AuthRequest;
import com.example.SecureAPI.dto.AuthResponse;
import com.example.SecureAPI.dto.RegisterRequest;
import com.example.SecureAPI.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для контроллера AuthController.
 * Проверяет работу эндпоинтов /auth/register и /auth/login.
 */
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Создаем MockMvc для standalone тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /**
     * Тестирует POST /auth/register.
     * Ожидается ответ 201 Created и вызов userService.register().
     */
    @Test
    void register_ShouldReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("Test User");
        request.setRole("USER");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"name\":\"Test User\",\"role\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered"));

        verify(userService, times(1)).register(request);
    }

    /**
     * Тестирует POST /auth/login.
     * Ожидается успешный ответ с JWT токеном, ролью и ID пользователя.
     */
    @Test
    void login_ShouldReturnAuthResponse() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        AuthResponse fakeResponse = new AuthResponse("fake-jwt-token", "USER", 1L);

        when(userService.login("test@example.com", "password")).thenReturn(fakeResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").value(1));
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