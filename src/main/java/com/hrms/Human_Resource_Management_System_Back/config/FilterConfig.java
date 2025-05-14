package com.hrms.Human_Resource_Management_System_Back.config;

import com.hrms.Human_Resource_Management_System_Back.middleware.SchemaRoutingFilter;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing filters used in the application.
 * <p>
 * This class registers filters like {@link SchemaRoutingFilter} for tenant schema routing.
 * </p>
 */
@Configuration
public class FilterConfig {

    /**
     * Registers the {@link SchemaRoutingFilter} to handle schema routing for tenants.
     * <p>
     * This filter inspects the JWT token and routes requests to the appropriate tenant schema.
     * </p>
     *
     * @param jwtService the JWT service used to decode and validate tokens
     * @return the configured {@link SchemaRoutingFilter}
     */
    @Bean
    SchemaRoutingFilter schemaRoutingFilter(JwtService jwtService) {
        return new SchemaRoutingFilter(jwtService);
    }
}