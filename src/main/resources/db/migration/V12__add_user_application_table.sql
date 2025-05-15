CREATE TABLE public.user_application (
    user_application_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES public.user(user_id) ON DELETE CASCADE,
    job_listing_id INT NOT NULL REFERENCES public.job_listing(job_listing_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
