package com.dembasiby.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-key-that-is-at-least-32-bytes-long-for-hmac", 86400000L);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtService.generateToken(1L, "test@example.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUserId_shouldReturnCorrectUserId() {
        String token = jwtService.generateToken(42L, "test@example.com");
        Long userId = jwtService.extractUserId(token);
        assertEquals(42L, userId);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(1L, "test@example.com");
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidToken() {
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() {
        JwtService shortLivedService = new JwtService(
                "test-secret-key-that-is-at-least-32-bytes-long-for-hmac", -1000L);
        String token = shortLivedService.generateToken(1L, "test@example.com");
        assertFalse(jwtService.isTokenValid(token));
    }
}
