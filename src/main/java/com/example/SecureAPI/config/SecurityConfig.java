package com.example.SecureAPI.config;

import com.example.SecureAPI.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;

/**
 * Класс SecurityConfig содержит настройки безопасности Spring Security.
 * Здесь настраиваются:
 * - Отключение CSRF (так как используется stateless аутентификация через JWT)
 * - Stateless-сессии (без сохранения состояния на сервере)
 * - Фильтр JWT-аутентификации
 * - Политики безопасности: Content-Security-Policy, Cache-Control
 * - Разграничение доступа по ролям пользователей
 */
@Configuration // Указывает, что в этом классе содержатся бины конфигурации
@EnableWebSecurity // Включает поддержку веб-безопасности Spring Security
@EnableMethodSecurity // Позволяет использовать аннотации @PreAuthorize / @PostAuthorize на уровне методов
public class SecurityConfig {

    /**
     * Основная цепочка фильтров безопасности.
     * Настройка HTTP-безопасности:
     * - CSRF отключен, так как используется stateless аутентификация
     * - Сессии не создаются (STATELESS)
     * - Добавлен кастомный фильтр JwtFilter перед стандартным UsernamePasswordAuthenticationFilter
     * - Настроены заголовки безопасности:
     *   - Content-Security-Policy: защита от XSS
     *   - Frame-Options: запрет отображения в iframe (защита от clickjacking)
     *   - Cache-Control: отключён, чтобы избежать кэширования чувствительных данных
     * - Разрешён доступ без аутентификации к эндпоинтам:
     *   - /auth/**
     *   - /swagger-ui/**
     *   - /v3/api-docs/**
     * - Все остальные запросы требуют аутентификации
     * - Только роли ADMIN и EMPLOYEE могут работать с эндпоинтами /products/**
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                // Отключаем CSRF, так как используем stateless аутентификацию через JWT
                .csrf(csrf -> csrf.disable())

                // Используем stateless сессии — Spring не будет создавать или использовать сессию HTTP
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Настраиваем заголовки безопасности
                .headers(headers -> headers
                        // Content-Security-Policy: ограничивает источники скриптов и стилей
                        .addHeaderWriter(new ContentSecurityPolicyHeaderWriter("script-src 'self'; object-src 'none'; style-src 'self'"))
                        // Запрещаем отображение сайта в iframe (защита от clickjacking)
                        .frameOptions(frame -> frame.sameOrigin())
                        // Отключаем кэширование чувствительных данных
                        .cacheControl(cache -> cache.disable()))

                // Добавляем наш кастомный фильтр JWT перед стандартным фильтром логина
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Настраиваем политики доступа к URL
                .authorizeHttpRequests(auth -> auth
                        // Все запросы к /auth/** разрешены без аутентификации (для регистрации и логина)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/users/public").permitAll()

                        // Только роли ADMIN и EMPLOYEE могут работать с товарами, кроме того, что находится в публичном доступе
                        .requestMatchers("/products/public").permitAll()
                        .requestMatchers("/products/public-add").permitAll()
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        // Разрешаем доступ к Swagger UI и API документации
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Публично
                        .requestMatchers("/api/cart/all", "/api/cart/public-add").permitAll()
                        .requestMatchers("/api/orders/all", "/api/orders/public-create").permitAll()

                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Бин для шифрования паролей с использованием BCrypt.
     * Используется при регистрации и сравнении паролей при логине.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}