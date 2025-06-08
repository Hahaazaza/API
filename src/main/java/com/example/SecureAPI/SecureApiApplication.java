package com.example.SecureAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в Spring Boot приложение.
 * Запускает встроенное веб-приложение.
 */
@SpringBootApplication
public class SecureApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureApiApplication.class, args);
	}

}