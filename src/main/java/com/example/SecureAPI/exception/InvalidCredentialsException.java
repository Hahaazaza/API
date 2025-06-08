package com.example.SecureAPI.exception;

/**
 * Исключение, выбрасываемое при некорректных учетных данных (логин/пароль).
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}