
ALTER TABLE public.tenant
    ADD COLUMN IF NOT EXISTS schema_name VARCHAR(64) UNIQUE NOT NULL default 'public';