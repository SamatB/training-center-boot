package com.training.trainingcenterboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Секретный ключ из application.properties
    //
    // Используется для:
    // - подписи JWT
    // - проверки JWT
    @Value("${jwt.secret}")
    private String secret;

    // Время жизни JWT token
    //
    // Например:
    // 86400000 = 24 часа
    @Value("${jwt.expiration}")
    private long expiration;

    // Создание JWT token
    public String generateToken(UserDetails userDetails) {

        // Текущее время
        Date now = new Date();

        // Время окончания token
        Date expiredAt = new Date(now.getTime() + expiration);

        // Создаем JWT
        return Jwts.builder()

                // Subject = username пользователя
                .subject(userDetails.getUsername())

                // Время создания token
                .issuedAt(now)

                // Время истечения token
                .expiration(expiredAt)

                // Подписываем token secret key
                .signWith(getSigningKey())

                // Собираем token в строку
                .compact();
    }

    // Получение username из JWT
    public String extractUsername(String token) {

        // Из payload JWT берем поле subject
        return extractAllClaims(token).getSubject();
    }

    // Проверка JWT token
    public boolean isValid(String token, UserDetails userDetails) {

        // Получаем username из token
        String username = extractUsername(token);

        // Проверяем:
        // 1. username совпадает?
        // 2. token не просрочен?
        return username.equals(userDetails.getUsername())
                && !isExpired(token);
    }

    // Проверка просрочен ли token
    private boolean isExpired(String token) {

        // Если expiration раньше текущего времени —
        // token просрочен
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Получение всех данных (claims) из JWT
    //
    // Claims:
    // subject
    // expiration
    // issuedAt
    // custom fields
    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                // Проверяем подпись через secret key
                .verifyWith(getSigningKey())

                // Создаем parser
                .build()

                // Парсим token
                .parseSignedClaims(token)

                // Получаем payload
                .getPayload();
    }

    // Создание SecretKey для подписи и проверки JWT
    // JWT работает так:
    // 1. При создании token:
    //    Spring подписывает JWT secret key
    // 2. При каждом request:
    //    Spring проверяет:
    //    - token настоящий?
    //    - token не подделан?
    // Для этого нужен SecretKey
    private SecretKey getSigningKey() {

        // Берем строку secret:
        // jwt.secret=mySecretKey...
        // и превращаем в массив байтов
        // UTF_8:
        // стандартная кодировка текста
        byte[] keyBytes =
                secret.getBytes(StandardCharsets.UTF_8);

        // Создаем HMAC SHA SecretKey
        // HS256:
        // HMAC + SHA256
        // Этот ключ будет использоваться:
        // - для подписи JWT
        // - для проверки подписи JWT
        return Keys.hmacShaKeyFor(keyBytes);
    }
}