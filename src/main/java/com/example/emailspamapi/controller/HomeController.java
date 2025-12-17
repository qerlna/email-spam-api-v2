package com.example.emailspamapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "service", "Email Spam API",
                "version", "1.0.0",
                "status", "running",
                "timestamp", LocalDateTime.now().toString(),
                "endpoints", Map.of(
                        "swagger", "/swagger-ui.html",
                        "docs", "/v3/api-docs",
                        "health", "/health",
                        "check-spam", "/api/email/check-spam"
                )
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "OK",
                "service", "email-spam-api",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}