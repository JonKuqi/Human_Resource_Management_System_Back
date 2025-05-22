package com.hrms.Human_Resource_Management_System_Back.middleware;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
/**
 * SchemaRoutingFilter  →  sets TenantContext(schemaFromJwt)
 * TenantIdentifierResolver  →  gives schema to Hibernate
 * TenantAwareConnectionProvider  →  SET search_path TO "schema"
 * JpaMultiTenantConfig  →  wires everything together
 *
 * <p>
 * This class resolves the current tenant identifier for the multi-tenant application and
 * provides it to Hibernate for routing to the appropriate tenant schema.
 * </p>
 */
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    /**
     * Resolves the current tenant identifier.
     * <p>
     * This method retrieves the tenant identifier from the {@link TenantCtx} context, which stores the current
     * tenant for the current thread (typically set via the {@link SchemaRoutingFilter} based on the JWT token).
     * If the tenant identifier is null or blank, it returns "public" as the default schema.
     * </p>
     *
     * @return the current tenant identifier (schema name)
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx.getTenant();
        System.out.println("___________________Resolving tenant: " + tenant);
        return (tenant == null || tenant.isBlank()) ? "public" : tenant;
    }

    /**
     * Validates if the current session is valid.
     * <p>
     * This method allows session switching during a transaction, if necessary.
     * </p>
     *
     * @return {@code true} to allow switching mid-transaction if needed
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true; // allows switching mid‑Tx if needed
    }
}