package com.hrms.Human_Resource_Management_System_Back.service;

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
 * Service for handling JWT (JSON Web Token) operations, including token generation, validation,
 * and extraction of claims.
 * <p>
 * This service provides utility methods for parsing JWT tokens, extracting claims (such as the subject or expiration),
 * validating tokens, and generating tokens with custom claims and expiration.
 * </p>
 */
@Service
public class JwtService {

    /**
     * The secret key used for signing and verifying JWT tokens.
     */
    @Value("${spring.jwt.secret}")
    private String secret;

    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Debug method to print the loaded JWT secret to the console after initialization.
     * <p>
     * This method is used to log the loaded JWT secret for debugging purposes.
     * </p>
     */
    @PostConstruct
    public void debugSecret() {
        System.out.println("JWT SECRET LOADED: " + secret);
    }

    // ─── Token Parsing ─────────────────────────────────────────────────────────

    /**
     * Parses the JWT token and returns all claims.
     * <p>
     * This method decodes the JWT token and extracts the claims using the secret key for verification.
     * </p>
     *
     * @param token the JWT token to parse
     * @return the claims contained within the JWT
     */
    public Claims parseToken(String token) {
        System.out.println("Parse Token secret: " + secret);
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Returns the JWT secret key.
     *
     * @return the JWT secret key
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function.
     * <p>
     * This method allows you to extract any claim from the parsed JWT token using the provided resolver function.
     * </p>
     *
     * @param token   the JWT token to extract the claim from
     * @param resolver the function used to extract the desired claim
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parseToken(token));
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date of the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if the token is expired.
     * <p>
     * This method compares the expiration date in the token with the current date.
     * </p>
     *
     * @param token the JWT token
     * @return {@code true} if the token is expired, otherwise {@code false}
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the JWT token against the provided user details.
     * <p>
     * This method ensures that the token is valid (i.e., the username in the token matches the provided
     * user details and the token is not expired).
     * </p>
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate the token against
     * @return {@code true} if the token is valid, otherwise {@code false}
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ─── Token Generation ─────────────────────────────────────────────────────

    /**
     * Returns the signing key used for generating and verifying JWT tokens.
     * <p>
     * The signing key is derived from the JWT secret configured in the application properties.
     * </p>
     *
     * @return the signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token with custom claims, subject, and expiration duration.
     * <p>
     * This method creates a JWT token using the provided custom claims, subject (typically username),
     * and an expiration duration. The token is signed using the configured secret key.
     * </p>
     *
     * @param extraClaims additional claims to be included in the token
     * @param subject the subject (e.g., username) for the token
     * @param duration the expiration duration for the token
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, String subject, Duration duration) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token for a {@link UserDetails} object with a custom expiration duration.
     *
     * @param userDetails the user details to create the token for
     * @param duration the expiration duration for the token
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails, Duration duration) {
        return generateToken(Map.of(), userDetails.getUsername(), duration);
    }

    /**
     * Generates a JWT token with custom claims and default 24-hour expiration.
     *
     * @param extraClaims additional claims to be included in the token
     * @param userDetails the user details to create the token for
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername(), Duration.ofHours(24));
    }

    /**
     * Generates a JWT token with custom claims and a specified expiration duration.
     * <p>
     * This method allows generating a token for a default subject, typically "system" or another fallback subject.
     * </p>
     *
     * @param claims the claims to include in the token
     * @param duration the expiration duration for the token
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> claims, Duration duration) {
        return generateToken(claims, "system", duration);
    }

    /**
     * Generates a JWT token for a {@link UserDetails} object with a default 24-hour expiration.
     *
     * @param userDetails the user details to create the token for
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Duration.ofHours(24));
    }
}