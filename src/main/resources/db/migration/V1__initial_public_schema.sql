
CREATE SCHEMA IF NOT EXISTS public;


-- Create ENUM types
-- Create ENUM types first


DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('GENERAL_USER', 'TENANT_USER', 'ADMIN');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE billing_cycle AS ENUM ('MONTHLY', 'YEARLY');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE subscription_status AS ENUM ('ACTIVE', 'INACTIVE', 'CANCELLED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE employment_type AS ENUM ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'TEMPORARY', 'INTERNSHIP');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;


-- Address must come first as it's referenced by others
CREATE TABLE IF NOT EXISTS public.address (
                                       address_id SERIAL PRIMARY KEY,
                                       country VARCHAR(100) NOT NULL,
                                       city VARCHAR(100) NOT NULL,
                                       street VARCHAR(255) NOT NULL,
                                       zip VARCHAR(20) NOT NULL
);

-- Tenant table (referenced by users)
CREATE TABLE IF NOT EXISTS public.tenant (
                                      tenant_id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) UNIQUE NOT NULL,
                                      contact_email VARCHAR(255) UNIQUE NOT NULL,
                                      address_id INT REFERENCES address(address_id),
                                      web_link VARCHAR(255),
                                      description TEXT,
                                      created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                      updated_at TIMESTAMP DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS public."user" (
                                             user_id SERIAL PRIMARY KEY,
                                             username VARCHAR(50) UNIQUE NOT NULL,
                                             email VARCHAR(255) UNIQUE NOT NULL,
                                             salt VARCHAR(300) NOT NULL,
                                             password_hash VARCHAR(400) NOT NULL,
                                             tenant_id INT REFERENCES tenant(tenant_id), -- Nullable for non-tenant users
                                             role user_role NOT NULL DEFAULT 'GENERAL_USER',
                                             created_at TIMESTAMP DEFAULT NOW() NOT NULL
);



-- Verification Codes
CREATE TABLE IF NOT EXISTS public.verification_code (
                                                 verification_id SERIAL PRIMARY KEY,
                                                 user_id INT NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
                                                 code_hash VARCHAR(6) NOT NULL,
                                                 created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                                 expires_at TIMESTAMP NOT NULL,
                                                 is_used BOOLEAN DEFAULT FALSE NOT NULL
);

-- User General Profile
CREATE TABLE IF NOT EXISTS public.user_general (
                                            user_general_id SERIAL PRIMARY KEY,
                                            user_id INT UNIQUE NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
                                            first_name VARCHAR(50) NOT NULL,
                                            last_name VARCHAR(50) NOT NULL,
                                            phone VARCHAR(20),
                                            gender VARCHAR(20),
                                            birth_date DATE,
                                            profile_photo BYTEA,
                                            cv BYTEA,
                                            portfolio BYTEA
);

-- Skills
CREATE TABLE IF NOT EXISTS public.skill (
                                     skill_id SERIAL PRIMARY KEY,
                                     type VARCHAR(50) NOT NULL,
                                     name VARCHAR(100) UNIQUE NOT NULL
);

-- User Skills Junction
CREATE TABLE IF NOT EXISTS public.user_skills (
                                           user_skills_id SERIAL PRIMARY KEY,
                                           user_general_id INT NOT NULL REFERENCES user_general(user_general_id) ON DELETE CASCADE,
                                           skill_id INT NOT NULL REFERENCES skill(skill_id) ON DELETE CASCADE,
                                           value INT CHECK (value BETWEEN 1 AND 100),
                                           issued_at DATE DEFAULT NOW() NOT NULL,
                                           valid_until DATE,
                                           UNIQUE(user_general_id, skill_id)
);

-- Tenant Bank Info
CREATE TABLE IF NOT EXISTS public.tenant_bank_info (
                                                tenant_bank_info_id SERIAL PRIMARY KEY,
                                                tenant_id INT UNIQUE NOT NULL REFERENCES tenant(tenant_id) ON DELETE CASCADE,
                                                card_holder_name VARCHAR(255) NOT NULL,
                                                card_last_four CHAR(4) NOT NULL CHECK (char_length(card_last_four) = 4),
                                                card_brand VARCHAR(50) NOT NULL,
                                                expiration_date DATE NOT NULL,
                                                billing_address TEXT NOT NULL,
                                                created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Subscription Plans
CREATE TABLE IF NOT EXISTS public.subscription (
                                            subscription_id SERIAL PRIMARY KEY,
                                            plan_name VARCHAR(255) UNIQUE NOT NULL,
                                            description TEXT,
                                            price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
                                            billing_cycle billing_cycle NOT NULL
);

-- Industries
CREATE TABLE IF NOT EXISTS public.industry (
                                        industry_id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) UNIQUE NOT NULL
);

-- Tenant Subscriptions
CREATE TABLE IF NOT EXISTS public.tenant_subscription (
                                                   tenant_subscription_id SERIAL PRIMARY KEY,
                                                   tenant_id INT NOT NULL REFERENCES tenant(tenant_id) ON DELETE CASCADE,
                                                   subscription_id INT NOT NULL REFERENCES subscription(subscription_id) ON DELETE CASCADE,
                                                   start_date DATE NOT NULL,
                                                   end_date DATE NOT NULL,
                                                   status subscription_status NOT NULL DEFAULT 'ACTIVE',
                                                   created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                                   CHECK (end_date > start_date)
);

-- Job Listings
CREATE TABLE IF NOT EXISTS public.job_listing (
                                           job_listing_id SERIAL PRIMARY KEY,
                                           tenant_id INT NOT NULL REFERENCES tenant(tenant_id) ON DELETE CASCADE,
                                           job_title VARCHAR(255) NOT NULL,
                                           industry_id INT REFERENCES industry(industry_id),
                                           custom_industry VARCHAR(255),
                                           location VARCHAR(255) NOT NULL,
                                           employment_type employment_type NOT NULL,
                                           description TEXT NOT NULL,
                                           about_us TEXT,
                                           salary_from NUMERIC(10,2) CHECK (salary_from >= 0),
                                           salary_to NUMERIC(10,2) CHECK (salary_to >= 0),
                                           valid_until DATE NOT NULL,
                                           created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                           CHECK (salary_to >= salary_from),
                                           CHECK (industry_id IS NOT NULL OR custom_industry IS NOT NULL)
);

-- Job Tags
CREATE TABLE IF NOT EXISTS public.job_tag (
                                       job_tag_id SERIAL PRIMARY KEY,
                                       job_listing_id INT NOT NULL REFERENCES job_listing(job_listing_id) ON DELETE CASCADE,
                                       tag_name VARCHAR(50) NOT NULL,
                                       UNIQUE(job_listing_id, tag_name)
);
