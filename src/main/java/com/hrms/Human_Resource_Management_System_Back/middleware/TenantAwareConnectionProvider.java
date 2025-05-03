package com.hrms.Human_Resource_Management_System_Back.middleware;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TenantAwareConnectionProvider
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {

    private final DataSource dataSource;

    public TenantAwareConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /* one shared pool */
    @Override
    protected DataSource selectAnyDataSource() { return dataSource; }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) { return dataSource; }

    /* per‑tenant handling */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        String schema = (tenantIdentifier == null || tenantIdentifier.isBlank())
                ? "public" : tenantIdentifier;
        conn.createStatement().execute("SET search_path TO \"" + schema + "\"");
        return conn;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection conn) throws SQLException {
        try { conn.createStatement().execute("RESET search_path"); }
        finally { DataSourceUtils.releaseConnection(conn, dataSource); }
    }

    @Override
    public boolean supportsAggressiveRelease() { return false; }
}