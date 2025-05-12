DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'user_general'
              AND column_name = 'verified'
        ) THEN
            ALTER TABLE public.user_general
                ADD COLUMN verified BOOLEAN DEFAULT FALSE NOT NULL;
        END IF;
    END$$;
ALTER TABLE public.verification_code
    ALTER COLUMN code_hash TYPE VARCHAR(300);