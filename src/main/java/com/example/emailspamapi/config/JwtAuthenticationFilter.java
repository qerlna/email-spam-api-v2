package com.example.emailspamapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // ВРЕМЕННО: не инжектим UserDetailsService, чтобы избежать зависимостей
    // @Autowired
    // private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Если нет заголовка Authorization, пропускаем дальше
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);

            // Простая валидация токена без UserDetailsService
            if (jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);

                // Создаем простой UserDetails для аутентификации
                // ВРЕМЕННО: используем заглушку вместо реального UserDetailsService
                UserDetails userDetails = User.builder()
                        .username(username)
                        .password("") // пустой пароль для JWT аутентификации
                        .authorities(Collections.emptyList())
                        .build();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Логируем ошибку, но продолжаем цепочку фильтров
            logger.debug("JWT authentication failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // Переопределяем, чтобы фильтр не применялся к определенным путям
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Не применяем фильтр к публичным эндпоинтам
        return path.startsWith("/api/auth/") ||
                path.startsWith("/api/email/check-spam") ||
                path.startsWith("/api/email/health") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/") ||
                path.equals("/health");
    }
}