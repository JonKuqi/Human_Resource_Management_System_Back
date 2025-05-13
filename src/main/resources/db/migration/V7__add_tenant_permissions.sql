CREATE TABLE IF NOT EXISTS public.tenant_permission(
    tenant_permission_id SERIAL PRIMARY KEY,
    name TEXT,
    verb TEXT,
    resource TEXT
);

