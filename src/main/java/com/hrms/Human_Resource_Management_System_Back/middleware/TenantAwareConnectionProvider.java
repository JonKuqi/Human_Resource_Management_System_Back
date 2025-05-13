package com.hrms.Human_Resource_Management_System_Back.middleware;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A connection provider that is aware of multi-tenancy by using tenant-specific schemas.
 * <p>
 * This class extends {@link AbstractDataSourceBasedMultiTenantConnectionProviderImpl} and is responsible for providing
 * database connections that are specific to a tenant. It allows the application to dynamically switch between different
 * schemas based on the tenant identifier extracted from the JWT or request context.
 * </p>
 */
public class TenantAwareConnectionProvider
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {

    /**
     * The shared data source used for all tenants.
     * This is a single data source, but the connection schema is switched per tenant.
     */
    private final DataSource dataSource;

    /**
     * Constructor for initializing the tenant-aware connection provider.
     * <p>
     * This constructor initializes the provider with a given shared data source for all tenants.
     * </p>
     *
     * @param dataSource the shared data source for all tenants
     */
    public TenantAwareConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns the shared data source for all tenants.
     * <p>
     * Since this is a multi-tenant application, the same data source is shared by all tenants.
     * </p>
     *
     * @return the shared data source
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    /**
     * Returns the shared data source for the specified tenant.
     * <p>
     * The tenant identifier is used to identify which schema should be selected in the database.
     * However, in this implementation, we use the same shared data source for all tenants and switch the schema.
     * </p>
     *
     * @param tenantIdentifier the identifier of the tenant
     * @return the shared data source for the specified tenant
     */
    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource;
    }

    /**
     * Provides a connection to the database with the schema set to the specified tenant identifier.
     * <p>
     * This method overrides the default connection provider behavior by executing an SQL statement to set the schema
     * based on the tenant identifier, ensuring that each tenant operates within its own schema in the database.
     * </p>
     *
     * @param tenantIdentifier the identifier of the tenant
     * @return a database connection with the tenant's schema set
     * @throws SQLException if an error occurs when establishing the connection or setting the schema
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        // Get a connection from the shared data source
        Connection conn = DataSourceUtils.getConnection(dataSource);

        // Set the schema for the tenant
        String schema = (tenantIdentifier == null || tenantIdentifier.isBlank())
                ? "public" // Default schema if tenant identifier is null or blank
                : tenantIdentifier; // Tenant-specific schema
        conn.createStatement().execute("SET search_path TO \"" + schema + "\"");

        return conn;
    }

    /**
     * Releases the connection and resets the schema.
     * <p>
     * After the request has been processed, this method ensures that the schema search path is reset to avoid
     * affecting other connections or tenants.
     * </p>
     *
     * @param tenantIdentifier the identifier of the tenant
     * @param conn the database connection to release
     * @throws SQLException if an error occurs while resetting the schema or releasing the connection
     */
    @Override
    public void releaseConnection(String tenantIdentifier, Connection conn) throws SQLException {
        try {
            // Reset the schema search path after use
            conn.createStatement().execute("RESET search_path");
        } finally {
            // Release the connection back to the shared data source
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    /**
     * Indicates whether aggressive release of connections is supported.
     * <p>
     * In this implementation, aggressive release is not supported, meaning the connection is not released
     * before the transaction is fully completed.
     * </p>
     *
     * @return {@code false}, indicating that aggressive release is not supported
     */
    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }
}