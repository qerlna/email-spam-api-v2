package com.example.emailspamapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Используем дефолтные значения на случай отсутствия properties
    private String secret = "defaultSecretKeyForDevelopment1234567890";
    private Long expiration = 86400000L; // 24 часа

    // Опциональные сеттеры для @Value - только если свойства существуют
    @Value("${jwt.secret:#{null}}")
    public void setSecret(String secret) {
        if (secret != null && !secret.isEmpty()) {
            this.secret = secret;
        }
    }

    @Value("${jwt.expiration:#{null}}")
    public void setExpiration(Long expiration) {
        if (expiration != null) {
            this.expiration = expiration;
        }
    }

    private SecretKey getSigningKey() {
        // Гарантируем, что ключ достаточной длины
        byte[] keyBytes;
        if (secret.length() < 32) {
            // Дополняем до 32 байт
            StringBuilder padded = new StringBuilder(secret);
            while (padded.length() < 32) {
                padded.append("0");
            }
            keyBytes = padded.substring(0, 32).getBytes();
        } else {
            keyBytes = secret.substring(0, 32).getBytes();
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true; // Если ошибка - считаем токен невалидным
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Добавляем только если authorities не null
        if (userDetails.getAuthorities() != null) {
            claims.put("role", userDetails.getAuthorities());
        }
        return createToken(claims, userDetails.getUsername());
    }

    // Перегруженный метод для простого использования
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Упрощенная валидация для тестов
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token) && extractUsername(token) != null;
        } catch (Exception e) {
            return false;
        }
    }
}