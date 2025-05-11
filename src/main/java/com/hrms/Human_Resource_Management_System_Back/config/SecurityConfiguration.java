package com.hrms.Human_Resource_Management_System_Back.config;


import com.hrms.Human_Resource_Management_System_Back.middleware.AuthorizationFilter;
import com.hrms.Human_Resource_Management_System_Back.middleware.JwtAuthenticationFilter;
import com.hrms.Human_Resource_Management_System_Back.middleware.SchemaRoutingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                                "/swagger-ui/*",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // then pick the tenant
                .addFilterBefore(schemaRoutingFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(authorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<SchemaRoutingFilter> schemaRoutingFilterRegistration(SchemaRoutingFilter filter) {
        FilterRegistrationBean<SchemaRoutingFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(1); // i pari
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistration(AuthorizationFilter filter) {
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(2); // i dyti
        return registration;
    }

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
}