INSERT INTO public.tenant_permission (name, verb, resource) VALUES
('CREATE_SUBSCRIPTION','POST','/api/v1/tenant/subscriptions/payments/create'),
('CREATE_JOB','POST','/api/v1/public/job-listing');

INSERT INTO public.industry (industry_id, name) VALUES
(1, 'Information Technology'),
(2, 'Healthcare'),
(3, 'Finance'),
(4, 'Education'),
(5, 'Manufacturing'),
(6, 'Retail'),
(7, 'Construction'),
(8, 'Transportation'),
(9, 'Energy'),
(10, 'Hospitality'),
(11, 'Telecommunications'),
(12, 'Agriculture'),
(13, 'Entertainment'),
(14, 'Real Estate'),
(15, 'Legal Services');
