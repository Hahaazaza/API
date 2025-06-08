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
 * SecurityConfig — это основной класс конфигурации Spring Security.
 *
 * Он настраивает:
 * - Отключение CSRF
 * - Stateless сессии (без сохранения состояния на сервере)
 * - Добавление JWT-фильтра
 * - Политики безопасности (Content-Security-Policy, Cache-Control)
 * - Разграничение доступа по URL и ролям
 * - Шифрование паролей через BCrypt
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Основной метод конфигурации цепочки фильтров безопасности.
     *
     * Здесь настраивается:
     * - Отключение CSRF
     * - Использование stateless сессий
     * - Настройка заголовков безопасности (CSP, Frame-Options, Cache-Control)
     * - Добавление кастомного фильтра JWT
     * - Разрешение доступа к публичным эндпоинтам
     * - Защита приватных эндпоинтов по ролям
     *
     * @param http   - объект HttpSecurity для настройки фильтров
     * @param jwtFilter - наш кастомный фильтр для обработки JWT
     * @return готовая цепочка фильтров
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                // Отключаем защиту CSRF, так как мы используем stateless аутентификацию через JWT
                .csrf(csrf -> csrf.disable())

                // Используем stateless сессии — не храним состояние между запросами
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

                // Добавляем наш фильтр JWT перед стандартным фильтром аутентификации
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Настройка политик доступа
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ без аутентификации к эндпоинтам:
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/users/public").permitAll()

                        // Публичный доступ к продуктам
                        .requestMatchers("/products/public").permitAll()
                        .requestMatchers("/products/public-add").permitAll()

                        // Только ADMIN и EMPLOYEE могут работать с /products/**
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        // Разрешаем Swagger UI и документацию API
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Публичные эндпоинты корзины и заказов
                        .requestMatchers("/api/cart/all", "/api/cart/public-add").permitAll()
                        .requestMatchers("/api/orders/all", "/api/orders/public-create").permitAll()

                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Бин для шифрования паролей с использованием BCrypt.
     *
     * BCrypt — это алгоритм хэширования с солью, обеспечивающий высокую степень защиты.
     * Он используется при регистрации новых пользователей и при сравнении паролей при логине.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}