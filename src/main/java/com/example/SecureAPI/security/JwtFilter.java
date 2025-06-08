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

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Пропустить /auth/** без проверки токена
        if (httpRequest.getRequestURI().startsWith("/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");

        // Проверяем, есть ли токен и валиден ли он
        if (token != null && jwtUtils.validateToken(token)) {
            String userId = jwtUtils.extractUserId(token);
            String role = jwtUtils.extractRole(token);

            // Создаем объект аутентификации
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            // Устанавливаем аутентификацию в контекст Spring Security
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Записываем userId в запрос для использования в MDC (логирование)
            request.setAttribute("userId", userId);
        }

        // Передаем запрос дальше по цепочке фильтров
        chain.doFilter(request, response);
    }
}