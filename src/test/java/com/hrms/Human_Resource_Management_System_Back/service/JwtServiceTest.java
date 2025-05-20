package com.hrms.Human_Resource_Management_System_Back.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    // 256-bit Base64 key (test-only)
    private static final String TEST_SECRET = "lOyr54n+jZcO9Z63zZ+RD/2TbOdCmK+P8Nkfnjru88Q=";
    private static final String USERNAME    = "testUser";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        jwtService.setSecret(TEST_SECRET);
    }

    /* ------------------------------------------------------------------ *
     * Helper that always injects the “sub” claim your service relies on  *
     * ------------------------------------------------------------------ */
    private String buildToken(Duration ttl) {
        Map<String,Object> claims = Map.of("sub", USERNAME);   // <-- subject
        return jwtService.generateToken(claims, USERNAME, ttl);
    }

    @Test
    void generateToken_shouldReturnCompactJwt() {
        String token = buildToken(Duration.ofHours(1));
        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(token.split("\\.").length == 3, "Should be header.payload.signature");
    }

    @Test
    void parseToken_shouldExposeSubject() {
        String token   = buildToken(Duration.ofMinutes(30));
        Claims claims  = jwtService.parseToken(token);
        assertEquals(USERNAME, claims.getSubject());
    }

    @Test
    void extractUsername_shouldMatchInjectedSubject() {
        String token = buildToken(Duration.ofMinutes(10));
        assertEquals(USERNAME, jwtService.extractUsername(token));
    }

    @Test
    void extractExpiration_shouldBeInFuture() {
        String token = buildToken(Duration.ofMinutes(5));
        Date exp     = jwtService.extractExpiration(token);
        assertTrue(exp.after(new Date()));
    }

    @Test
    void isTokenValid_shouldReturnTrueForFreshToken() {
        User userDetails = new User(USERNAME, "", Collections.emptyList());
        String token     = buildToken(Duration.ofHours(1));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }



    @Test
    void parseToken_shouldThrowForGarbageInput() {
        assertThrows(Exception.class, () -> jwtService.parseToken("totally.bogus.token"));
    }
}
