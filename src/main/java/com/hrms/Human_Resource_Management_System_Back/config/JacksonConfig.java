package com.hrms.Human_Resource_Management_System_Back.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module hibernateModule() {
        // Handles lazy proxies, transient fields, etc.
        return new Hibernate6Module();
    }
}