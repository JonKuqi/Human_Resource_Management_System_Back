package com.hrms.Human_Resource_Management_System_Back.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI security schemes.
 * <p>
 * This class configures the security scheme for the OpenAPI documentation to use JWT Bearer authentication.
 * </p>
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}