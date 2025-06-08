package com.example.SecureAPI.service;

import com.example.SecureAPI.dto.AuthResponse;
import com.example.SecureAPI.dto.RegisterRequest;
import com.example.SecureAPI.model.User;

import java.util.List;

public interface UserService {
    void register(RegisterRequest request);
    AuthResponse login(String email, String password); // Теперь возвращает AuthResponse
    List<User> getAllUsers();
}