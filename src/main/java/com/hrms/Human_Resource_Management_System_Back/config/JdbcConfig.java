package com.hrms.Human_Resource_Management_System_Back.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration class for setting up JDBC components.
 * <p>
 * This class configures a {@link JdbcTemplate} bean to simplify database interactions using JDBC.
 * </p>
 */
@Configuration
public class JdbcConfig {

    /**
     * Registers the {@link JdbcTemplate} bean to facilitate database operations.
     * <p>
     * The {@link JdbcTemplate} provides an abstraction layer over raw JDBC, simplifying operations like querying,
     * updating, and executing SQL commands with automatic resource management.
     * </p>
     *
     * @param ds the {@link DataSource} to be used by the {@link JdbcTemplate}
     * @return the configured {@link JdbcTemplate} instance
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}