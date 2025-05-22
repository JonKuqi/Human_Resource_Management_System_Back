package com.hrms.Human_Resource_Management_System_Back.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Jackson modules.
 * <p>
 * This class registers the {@link Hibernate6Module} to ensure proper serialization of Hibernate entities.
 * </p>
 */
@Configuration
public class JacksonConfig {

    /**
     * Registers the Hibernate6Module to handle serialization of Hibernate entities.
     * <p>
     * This module ensures that lazy-loaded proxies, transient fields, and other Hibernate-specific features
     * are correctly handled during JSON serialization.
     * </p>
     *
     * @return the configured {@link Hibernate6Module} instance
     */
    @Bean
    public Module hibernateModule() {
        // Handles lazy proxies, transient fields, etc.
        return new Hibernate6Module();
    }
}