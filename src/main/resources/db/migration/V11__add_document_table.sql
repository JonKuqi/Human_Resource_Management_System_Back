CREATE TABLE IF NOT EXISTS public.document (
    document_id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    data BYTEA NOT NULL
);
