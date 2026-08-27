package com.bookstore.bookstore.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Normal login = 1 hour
    @Value("${jwt.expiration}")
    private long normalExpirationTime;

    // Remember Me = 30 days
    @Value("${jwt.remember-me-expiration}")
    private long rememberMeExpirationTime;

    // ================= SIGNING KEY =================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ================= GENERATE TOKEN =================

    // rememberMe = false -> 1 hour
    // rememberMe = true  -> 30 days

    public String generateToken(
            String email,
            boolean rememberMe) {

        long expirationTime =
                rememberMe
                        ? rememberMeExpirationTime
                        : normalExpirationTime;

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expirationTime
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // ================= DEFAULT TOKEN =================

    // Used when Remember Me is not provided
    // Default = normal 1 hour token

    public String generateToken(String email) {

        return generateToken(email, false);
    }

    // ================= EXTRACT EMAIL =================

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // ================= VALIDATE TOKEN =================

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}