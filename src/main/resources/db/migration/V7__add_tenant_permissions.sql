CREATE TABLE IF NOT EXISTS public.tenant_permissions(
    tenant_permission_id SERIAL PRIMARY KEY,
    name TEXT,
    verb TEXT,
    resource TEXT
);

