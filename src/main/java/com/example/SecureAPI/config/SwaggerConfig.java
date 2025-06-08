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
 * Этот класс определяет базовую информацию об API, такую как название,
 * описание, версию, контактные данные и лицензию. Также здесь настраивается
 * механизм безопасности через JWT Bearer токены.
 * Находится в процессе доработки
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