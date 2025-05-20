package com.hrms.Human_Resource_Management_System_Back.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrms.Human_Resource_Management_System_Back.exception.EmailNotVerifiedException;
import com.hrms.Human_Resource_Management_System_Back.middleware.AuthorizationFilter;
import com.hrms.Human_Resource_Management_System_Back.middleware.JwtAuthenticationFilter;
import com.hrms.Human_Resource_Management_System_Back.middleware.SchemaRoutingFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Security configuration for the application.
 * Defines authentication and authorization rules.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final SchemaRoutingFilter schemaRoutingFilter;
    private final AuthorizationFilter authorizationFilter;


    /**
     * Configures the security filter chain.
     * <p>
     * - Disables CSRF protection.
     * - Allows unauthenticated access to endpoints under `/api/auth/**`.
     * - Requires authentication for all other requests.
     * - Uses stateless session management.
     * - Adds a JWT authentication filter before the {@link UsernamePasswordAuthenticationFilter}.
     * </p>
     *
     * @param http the {@link HttpSecurity} object to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs while configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                "/api/v1/public/user/authenticate",
                                "/api/v1/public/user-general/register",
                                //"/api/public/user-general",
                                "/api/v1/tenant/user-tenant/register",
                                "/api/v1/public/tenant/**",
                                "/api/v1/public/user-general/verify",
                                "/api/v1/public/user-general/resend",
                                "/api/v1/public/subscriptions/**",
                                "/api/v1/public/permission",
                                "/api/v1/public/job-listing",
                                "/api/v1/public/industry",
                                //"/api/v1/public/user/filter",
                                "/swagger-ui/*",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint())
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // then pick the tenant
                .addFilterBefore(schemaRoutingFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(authorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Registers the schema routing filter to be applied first.
     * Used to dynamically route to the correct tenant schema based on the request.
     *
     * @param filter the schema routing filter
     * @return the configured filter registration bean
     */
    @Bean
    public FilterRegistrationBean<SchemaRoutingFilter> schemaRoutingFilterRegistration(SchemaRoutingFilter filter) {
        FilterRegistrationBean<SchemaRoutingFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(1); // i pari
        return registration;
    }

    /**
     * Registers the authorization filter to be applied after schema is resolved.
     * Used to enforce role-based access control per tenant.
     *
     * @param filter the authorization filter
     * @return the configured filter registration bean
     */
    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistration(AuthorizationFilter filter) {
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(2); // i dyti
        return registration;
    }

    /**
     * Configures the CORS policy to allow cross-origin requests.
     * <p>
     * - Allows all origins.<br>
     * - Supports common HTTP methods and headers.<br>
     * - Enables credential sharing for cookies or auth headers.
     * </p>
     *
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.addAllowedOriginPattern("*");
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));          // includes  Authorization
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
/**`
     * Configures the authentication entry point for handling unauthorized access.
     * <p>
     * - Returns a JSON response with a 401 status code.
     * - Includes a timestamp and error message in the response body.
     * </p>
     *
     * @return the configured authentication entry point
     */
@Bean
public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
        if (!response.isCommitted()) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);

            // Check for EmailNotVerifiedException
            if (authException instanceof EmailNotVerifiedException) {
                errorResponse.put("message", "Email not verified.");
                errorResponse.put("error", "Email not verified");
            } else {
                errorResponse.put("message", "Authentication failed.");
                errorResponse.put("error", "UNAUTHORIZED");
            }

            mapper.writeValue(response.getOutputStream(), errorResponse);
            response.flushBuffer();
        }
    };
}

    /**
     * Configures the ObjectMapper for JSON serialization.
     * <p>
     * - Registers the JavaTimeModule to handle Java 8 date/time types.
     * - Disables writing dates as timestamps.
     * </p>
     *
     * @return the configured ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }


}