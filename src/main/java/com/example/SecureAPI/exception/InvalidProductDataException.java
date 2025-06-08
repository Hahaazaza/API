package com.example.SecureAPI.exception;

/**
 * Исключение, выбрасываемое при некорректных данных о продукте.
 * Например, отрицательная цена или пустое имя.
 */
public class InvalidProductDataException extends RuntimeException {
    public InvalidProductDataException(String message) {
        super(message);
    }
}