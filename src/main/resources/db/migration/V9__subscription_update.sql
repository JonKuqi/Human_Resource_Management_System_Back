-- change from enum to text
ALTER TABLE public.tenant_subscription
ALTER COLUMN status TYPE VARCHAR
USING status::text;


ALTER TABLE public.subscription DROP CONSTRAINT subscription_plan_name_key;

-- added column
ALTER TABLE public.subscription ADD COLUMN max_users INTEGER;

-- added some static prices
INSERT INTO public.subscription (plan_name, billing_cycle, price, description)
VALUES
    ('FREE', 'MONTHLY', 0.00, '5 employees, limited features'),
    ('BASIC', 'MONTHLY', 5.99, '10 employees, email support'),
    ('BASIC', 'YEARLY', 59.99, '10 employees, email support'),
    ('PRO', 'MONTHLY', 14.99, '50 employees, priority support'),
    ('PRO', 'YEARLY', 149.99, '50 employees, priority support');

-- needded for subscription plan
UPDATE public.subscription
SET max_users = CASE
                    WHEN plan_name = 'FREE' THEN 5
                    WHEN plan_name = 'BASIC' THEN 10
                    WHEN plan_name = 'PRO' THEN 50
    END;
