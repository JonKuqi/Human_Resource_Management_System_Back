
ALTER TABLE public.user_general ADD COLUMN verified BOOLEAN DEFAULT FALSE NOT NULL;
ALTER TABLE public.verification_code
      ALTER COLUMN code_hash TYPE VARCHAR(300);