package com.example.SecureAPI.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки Swagger (OpenAPI) документации.
 * <p>
 * Данный класс определяет:
 * - Базовую информацию о REST API: название, версия, описание, контактные данные, лицензию.
 * - Механизм безопасности через JWT Bearer токены.
 * <p>
 * Используется для автоматической генерации интерактивной документации API,
 * доступной по адресу:
 * <a href="http://localhost:8081/swagger-ui/index.html">http://localhost:8080/swagger-ui/index.html</a>
 * <p>
 * OpenAPI спецификация также доступна в формате JSON по адресу:
 * <a href="http://localhost:8081/v3/api-docs">http://localhost:8080/v3/api-docs</a>
 * <p>
 * На данный момент конфигурация находится в стадии доработки — планируется расширение
 * возможностей документирования эндпоинтов, параметров запросов, ответов и ошибок.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Secure REST API",
                version = "v1",
                description = "Документация к защищённому REST API сервису с аутентификацией через JWT",
                contact = @Contact(
                        name = "Hahaazaza",
                        email = "hahaazaza@example.com",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Bearer токен для доступа к защищённым ресурсам",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}