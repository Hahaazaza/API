package com.example.SecureAPI.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

/**
 * JwtFilter — это фильтр Spring Security, который отвечает за обработку JWT-токенов в каждом запросе.
 *
 * Он проверяет наличие токена в заголовке "Authorization", извлекает из него данные пользователя,
 * аутентифицирует его и устанавливает в контекст безопасности Spring Security (SecurityContextHolder).
 *
 * Это позволяет системе определить, кто делает запрос, и проверить, имеет ли пользователь доступ к запрашиваемому ресурсу.
 */
@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Метод doFilter выполняется для каждого HTTP-запроса приложения.
     *
     * Логика:
     * 1. Проверяем URI запроса — если он начинается с "/auth/", пропускаем фильтр (не требуем токен).
     * 2. Извлекаем токен из заголовка "Authorization".
     * 3. Если токен валиден, извлекаем из него userId и роль.
     * 4. Создаем объект UsernamePasswordAuthenticationToken — это Spring Security-объект аутентификации.
     * 5. Устанавливаем аутентификацию в SecurityContextHolder.
     * 6. Добавляем userId в атрибуты запроса, чтобы использовать его в логах через MDC.
     * 7. Передаем управление следующему фильтру цепочки.
     *
     * @param request  - входящий запрос
     * @param response - исходящий ответ
     * @param chain    - цепочка фильтров Spring Security
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Приводим общий ServletRequest к HttpServletRequest, чтобы получить URL и заголовки
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Получаем URI запроса — например: /auth/login или /api/products
        String requestURI = httpRequest.getRequestURI();

        // Пропускаем Swagger UI и OpenAPI документацию без проверки токена
        if (requestURI.startsWith("/swagger-ui/") || requestURI.startsWith("/v3/api-docs/")) {
            // Эти пути не требуют JWT-токена — разрешаем проход
            chain.doFilter(request, response); // используем переданный chain
            return; // выходим из метода после передачи управления дальше
        }

        // Пропускаем эндпоинты аутентификации (/auth/**) без проверки токена
        if (requestURI.startsWith("/auth/")) {
            // Регистрация и логин не требуют токена — разрешаем проход
            chain.doFilter(request, response);
            return; // выходим из метода после передачи управления дальше
        }

        // Извлекаем токен из заголовка Authorization
        String token = httpRequest.getHeader("Authorization");

        // Проверяем, есть ли токен и валиден ли он
        if (token != null && jwtUtils.validateToken(token)) {
            // Извлекаем ID пользователя из токена
            String userId = jwtUtils.extractUserId(token);

            // Извлекаем роль пользователя из токена
            String role = jwtUtils.extractRole(token);

            // Создаем объект аутентификации Spring Security:
            // - principal — идентификатор пользователя (userId)
            // - credentials — обычно пароль, но здесь не используется
            // - authorities — список прав доступа, ROLE_ADMIN / ROLE_CLIENT и т.д.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            // Устанавливаем аутентификацию в контекст Spring Security.
            // Теперь Spring знает, кто делает запрос и какие у него права.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Записываем userId в атрибуты запроса, чтобы использовать его в логах (через MDC).
            request.setAttribute("userId", userId);
        }

        // После обработки токена (или его отсутствия) передаем запрос дальше по цепочке фильтров.
        // Это может быть следующий фильтр Spring Security или сам контроллер.
        chain.doFilter(request, response);
    }
}