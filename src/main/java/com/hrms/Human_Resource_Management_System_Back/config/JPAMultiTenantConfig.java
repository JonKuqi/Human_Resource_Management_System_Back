package com.hrms.Human_Resource_Management_System_Back.config;


import com.hrms.Human_Resource_Management_System_Back.middleware.TenantAwareConnectionProvider;
import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
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

/**
 * Configuration class for setting up multi-tenancy with JPA and Hibernate.
 * <p>
 * This class configures the necessary beans to support multi-tenancy using the SCHEMA strategy in Hibernate.
 * </p>
 */
@Configuration
@EnableTransactionManagement
public class JPAMultiTenantConfig {

    /**
     * Registers the {@link CurrentTenantIdentifierResolver} bean, which is responsible for determining
     * the current tenant identifier for each request.
     * <p>
     * This resolver reads the tenant identifier from the {@link TenantCtx
     * text}, which stores the
     * tenant information for the current request thread.
     * </p>
     *
     * @return the configured {@link CurrentTenantIdentifierResolver} instance
     */
    @Bean
    public CurrentTenantIdentifierResolver tenantIdentifierResolver() {
        return new TenantIdentifierResolver();   // the small class we wrote
    }

    /**
     * Registers the {@link MultiTenantConnectionProvider} bean to provide tenant-aware database connections.
     * <p>
     * The provider ensures that each tenant uses its own schema, connecting to the correct database
     * schema based on the tenant identifier.
     * </p>
     *
     * @param dataSource the default {@link DataSource} used for tenant connections
     * @return the configured {@link MultiTenantConnectionProvider} instance
     */
    @Bean
    public MultiTenantConnectionProvider tenantConnectionProvider(DataSource dataSource) {
        return new TenantAwareConnectionProvider(dataSource);
    }

    /**
     * Configures the {@link LocalContainerEntityManagerFactoryBean} to set up JPA entity management.
     * <p>
     * This bean sets up Hibernate for multi-tenancy with the SCHEMA strategy, using the custom
     * {@link MultiTenantConnectionProvider} and {@link CurrentTenantIdentifierResolver}.
     * </p>
     *
     * @param builder the {@link EntityManagerFactoryBuilder} used to configure the entity manager
     * @param dataSource the {@link DataSource} to be used by JPA
     * @param connectionProvider the {@link MultiTenantConnectionProvider} responsible for tenant connections
     * @param resolver the {@link CurrentTenantIdentifierResolver} used to resolve the current tenant identifier
     * @return the configured {@link LocalContainerEntityManagerFactoryBean} for multi-tenant JPA configuration
     */
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
                .packages("com.hrms.Human_Resource_Management_System_Back.model",
                        "com.hrms.Human_Resource_Management_System_Back.model.tenant")
                .properties(props)
                .build();
    }
}