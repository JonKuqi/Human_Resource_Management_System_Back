package com.hrms.Human_Resource_Management_System_Back.config;


import com.hrms.Human_Resource_Management_System_Back.middleware.TenantAwareConnectionProvider;
import com.hrms.Human_Resource_Management_System_Back.middleware.TenantIdentifierResolver;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class JPAMultiTenantConfig {

    /* 1.  CurrentTenantIdentifierResolver reads value from TenantContext */
    @Bean
    public CurrentTenantIdentifierResolver tenantIdentifierResolver() {
        return new TenantIdentifierResolver();   // the small class we wrote
    }

    /* 2.  Our TenantAwareConnectionProvider */
    @Bean
    public MultiTenantConnectionProvider tenantConnectionProvider(DataSource dataSource) {
        return new TenantAwareConnectionProvider(dataSource);
    }

    /* 3.  Tell Hibernate to use SCHEMA strategy */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource,
            MultiTenantConnectionProvider connectionProvider,
            CurrentTenantIdentifierResolver resolver) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.multiTenancy", "SCHEMA");
        props.put("hibernate.multi_tenant_connection_provider", connectionProvider);
        props.put("hibernate.tenant_identifier_resolver", resolver);

        return builder
                .dataSource(dataSource)
                .packages(  "com.hrms.Human_Resource_Management_System_Back.model",
                        "com.hrms.Human_Resource_Management_System_Back.model.tenant")
                .properties(props)
                .build();
    }
}
