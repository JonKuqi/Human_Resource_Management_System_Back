package com.hrms.Human_Resource_Management_System_Back.config;

import com.hrms.Human_Resource_Management_System_Back.middleware.SchemaRoutingFilter;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    SchemaRoutingFilter schemaRoutingFilter(JwtService jwtService) {
        return new SchemaRoutingFilter(jwtService);
    }
}