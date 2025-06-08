package com.example.SecureAPI.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * JwtUtils — это вспомогательный класс для работы с JWT-токенами.
 *
 * Он предоставляет методы:
 * - Генерация токена
 * - Извлечение данных из токена (userId, роль)
 * - Валидация токена
 *
 * Все операции производятся с использованием секретного ключа и алгоритма HS512.
 */
@Component
public class JwtUtils {

    /**
     * Секретная строка используется для создания подписи токена.
     * Она должна быть достаточно длинной (> 512 бит), чтобы обеспечить безопасность.
     * Здесь мы используем Base64-кодированный ключ длиной 64 байта (512 бит).
     */
    private final String SECRET_STRING = "VGhpcyBpcyBhIHNlY3VyZSByYW5kb20gc2VjcmV0IGtleSBmb3IgSFNGMTI4IHdoaWNoIGlzIHN1ZmZpY2llbnRseSBsb25nIGVuY291ZGggdG8gdXNlIHdpdGggSFN4IGFsZ29yaXRobXM=";

    /**
     * Декодируем секретную строку в массив байтов для дальнейшего использования.
     */
    private final byte[] secretBytes = Base64.getDecoder().decode(SECRET_STRING);

    /**
     * Создаем ключ для подписи и верификации токенов.
     * Алгоритм HS512 требует SecretKeySpec с правильным JCA-именем алгоритма.
     */
    private final Key SIGNING_KEY = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS512.getJcaName());

    /**
     * Срок действия токена — 24 часа (в миллисекундах).
     */
    private final long EXPIRATION = 86400000; // 24 часа

    /**
     * Генерирует JWT-токен для пользователя.
     *
     * Токен содержит:
     * - subject (ID пользователя)
     * - claim "role" (роль пользователя)
     * - время истечения
     * - подпись с использованием HS512
     *
     * @param userId ID пользователя
     * @param role   роль пользователя (ADMIN, CLIENT, EMPLOYEE)
     * @return JWT-токен в виде строки
     */
    public String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Извлекает ID пользователя из JWT-токена.
     *
     * @param token JWT-токен
     * @return ID пользователя
     */
    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Извлекает роль пользователя из JWT-токена.
     *
     * @param token JWT-токен
     * @return роль пользователя
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * Проверяет валидность JWT-токена.
     *
     * @param token JWT-токен
     * @return true, если токен валиден, иначе false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}