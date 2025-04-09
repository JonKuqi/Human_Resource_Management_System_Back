
ALTER TABLE public.tenant
    ADD COLUMN schema_name VARCHAR(64) UNIQUE NOT NULL default 'public';