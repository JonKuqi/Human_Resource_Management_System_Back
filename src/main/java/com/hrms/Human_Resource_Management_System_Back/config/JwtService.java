package com.hrms.Human_Resource_Management_System_Back.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for handling JWT operations, including token generation, validation, and extraction of claims.
 */
@Service
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String secret;

    @PostConstruct
    public void debugSecret() {
        System.out.println(" JWT SECRET LOADED: " + secret);
    }


    //private static final String SECRET_KEY = "df47567e6a3386751269a976c692869a78f1a8364eb643c66cd05daecd7dabd0";

    // ─── Token Parsing ─────────────────────────────────────────────────────────

    /** Parse the JWT and return all claims. */
    public Claims parseToken(String token) {
        System.out.println("Parse Token secret: "+secret);
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }
    public String getSecret() {
        return secret;
    }

    /** Extract a single claim using a resolver function. */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parseToken(token));
    }

    /** Extract the username (subject). */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extract the expiration date. */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Check if token is expired. */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Validate token against UserDetails. */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ─── Token Generation ─────────────────────────────────────────────────────

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** Generate a token with custom claims, subject, and expiration duration. */
    public String generateToken(Map<String, Object> extraClaims, String subject, Duration duration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration.toMillis());
        System.out.println("Here");

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Generate a token for a UserDetails with a custom duration. */
    public String generateToken(UserDetails userDetails, Duration duration) {
        return generateToken(Map.of(), userDetails.getUsername(), duration);
    }

    /** Generate a token with custom claims and default 24-hour expiration. */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername(), Duration.ofHours(24));
    }

    public String generateToken(Map<String, Object> claims, Duration duration) {
        return generateToken(claims, "system", duration);  // or another default subject if you want
    }

    /** Generate a token for a UserDetails with default 24-hour expiration. */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Duration.ofHours(24));
    }
}
