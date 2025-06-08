package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.AuthResponse;
import com.example.SecureAPI.dto.RegisterRequest;
import com.example.SecureAPI.model.User;

import java.util.List;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface UserService {
    void register(RegisterRequest request);
    AuthResponse login(String email, String password);
    List<User> getAllUsers();
}