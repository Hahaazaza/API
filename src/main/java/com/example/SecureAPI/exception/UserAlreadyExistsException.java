package com.example.SecureAPI.exception;

/**
 * Исключение, выбрасываемое при попытке зарегистрировать уже существующего пользователя.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}