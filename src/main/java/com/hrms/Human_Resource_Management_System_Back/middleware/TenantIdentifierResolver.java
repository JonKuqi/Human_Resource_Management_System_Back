package com.hrms.Human_Resource_Management_System_Back.middleware;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
/**
 * SchemaRoutingFilter  →  sets TenantContext(schemaFromJwt)
 * TenantIdentifierResolver  →  gives schema to Hibernate
 * TenantAwareConnectionProvider  →  SET search_path TO "schema"
 * JpaMultiTenantConfig  →  wires everything together
 *
 */
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx.getTenant();
        System.out.println("___________________Resolving tenant: " + tenant);
        return (tenant == null || tenant.isBlank()) ? "public" : tenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;          // allows switching mid‑Tx if needed
    }
}