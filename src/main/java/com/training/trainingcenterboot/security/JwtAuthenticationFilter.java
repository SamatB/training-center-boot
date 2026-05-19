package com.training.trainingcenterboot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Сервис для работы с JWT:
    // создание token, проверка token, получение username
    private final JwtService jwtService;

    // Сервис загрузки пользователя из БД
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Получаем Authorization header
        String authHeader = request.getHeader("Authorization");

        // Проверяем:
        // 1. Header существует?
        // 2. Начинается ли с Bearer?
        //
        // Если нет:
        // пропускаем request дальше без JWT проверки
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Убираем "Bearer "
        //
        // Было:
        // Bearer eyJhbGciOi...
        //
        // Станет:
        // eyJhbGciOi...
        String token = authHeader.substring(7);

        // Получаем username из JWT token
        String username = jwtService.extractUsername(token);

        // Проверяем:
        // username найден?
        // и пользователь еще НЕ authenticated?
        //
        // SecurityContextHolder хранит текущего пользователя
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Загружаем пользователя из БД
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // Проверяем token:
            // - не просрочен ли
            // - принадлежит ли пользователю
            if (jwtService.isValid(token, userDetails)) {

                // Создаем Authentication объект Spring Security
                //
                // Он содержит:
                // - пользователя
                // - роли
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Добавляем дополнительные детали request
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Кладем Authentication в SecurityContext
                //
                // После этого Spring Security понимает:
                // - кто пользователь
                // - какая роль
                // - можно ли пускать дальше
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        // Передаем request дальше:
        // в следующий filter или controller
        filterChain.doFilter(request, response);
    }
}