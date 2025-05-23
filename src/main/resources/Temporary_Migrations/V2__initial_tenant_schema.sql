
-- User Tenant Table
CREATE TABLE IF NOT EXISTS user_tenant (
                                           user_tenant_id SERIAL PRIMARY KEY,
                                           user_id INT NOT NULL REFERENCES public.user(user_id) ON DELETE CASCADE,
                                           tenant_id INT NOT NULL REFERENCES public.tenant(tenant_id) ON DELETE CASCADE,
                                           first_name VARCHAR(50) NOT NULL,
                                           last_name VARCHAR(50) NOT NULL,
                                           phone VARCHAR(20) NOT NULL,
                                           gender VARCHAR(20),
                                           profile_photo BYTEA,
                                           address_id INT NOT NULL REFERENCES public.address(address_id)  ON DELETE CASCADE,
                                           created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE IF NOT EXISTS document (
    document_id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    data BYTEA NOT NULL
);


-- Application Table
CREATE TABLE IF NOT EXISTS application (
                                           application_id SERIAL PRIMARY KEY,
                                           job_listing_id INT NOT NULL REFERENCES public.job_listing(job_listing_id)  ON DELETE CASCADE,
                                           applicant_name VARCHAR(100) NOT NULL,
                                           applicant_email VARCHAR(255) NOT NULL,
                                           applicant_gender VARCHAR(20),
                                           applicant_birth_date DATE,
                                           applicant_phone VARCHAR(20) NOT NULL,
                                           experience TEXT,
                                           applicant_comment TEXT,
                                           cv_document_id INT REFERENCES document(document_id)  ON DELETE CASCADE,
                                           portfolio BYTEA,
                                           time_of_application TIMESTAMP DEFAULT NOW() NOT NULL,
                                           hr_comment TEXT,
                                           status VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
                                           created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                           updated_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Department Table
CREATE TABLE IF NOT EXISTS department (
                                          department_id SERIAL PRIMARY KEY,
                                          name VARCHAR(100) UNIQUE NOT NULL,
                                          description TEXT,
                                          created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Role Table
CREATE TABLE IF NOT EXISTS role (
                                    role_id SERIAL PRIMARY KEY,
                                    role_name VARCHAR(50) UNIQUE NOT NULL,
                                    description TEXT,
                                    created_at TIMESTAMP DEFAULT NOW()
);

-- User Role Junction Table
CREATE TABLE IF NOT EXISTS user_role_table (
                                         user_role_id SERIAL PRIMARY KEY,
                                         user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id)  ON DELETE CASCADE,
                                         role_id INT NOT NULL REFERENCES role(role_id)  ON DELETE CASCADE,
                                         UNIQUE(user_tenant_id, role_id)
);


-- Role Permission Junction Table
CREATE TABLE IF NOT EXISTS role_permission (
                                               role_permission_id SERIAL PRIMARY KEY,
                                               role_id INT NOT NULL REFERENCES role(role_id)  ON DELETE CASCADE,
                                               permission_id INT NOT NULL REFERENCES public.tenant_permission(tenant_permission_id)  ON DELETE CASCADE,
                                               target_role_id INT NULL REFERENCES role(role_id)  ON DELETE CASCADE

);

-- Position Table
CREATE TABLE IF NOT EXISTS position (
                                        position_id SERIAL PRIMARY KEY,
                                        department_id INT NOT NULL REFERENCES department(department_id)  ON DELETE CASCADE,
                                        title VARCHAR(100) NOT NULL,
                                        description TEXT,
                                        created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Contract Table
CREATE TABLE IF NOT EXISTS contract (
                                        contract_id SERIAL PRIMARY KEY,
                                        user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id) ON DELETE CASCADE,
                                        position_id INT NOT NULL REFERENCES position(position_id) ON DELETE CASCADE,
                                        contract_type VARCHAR(50) NOT NULL,
                                        start_date DATE NOT NULL,
                                        end_date DATE NOT NULL,
                                        salary NUMERIC(12,2) CHECK (salary > 0),
                                        terms TEXT NOT NULL,
                                        created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Payroll Table
CREATE TABLE IF NOT EXISTS payroll (
                                       payroll_id SERIAL PRIMARY KEY,
                                       user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id) ON DELETE CASCADE,
                                       pay_period_start DATE NOT NULL,
                                       pay_period_end DATE NOT NULL,
                                       base_salary NUMERIC(12,2) CHECK (base_salary >= 0),
                                       bonuses NUMERIC(12,2) CHECK (bonuses >= 0),
                                       deductions NUMERIC(12,2) CHECK (deductions >= 0),
                                       net_pay NUMERIC(12,2) CHECK (net_pay >= 0),
                                       payment_date DATE NOT NULL,
                                       created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                       CHECK (pay_period_end > pay_period_start)
);

-- Leave Request Table
CREATE TABLE IF NOT EXISTS leave_request (
                                             leave_request_id SERIAL PRIMARY KEY,
                                             user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id)  ON DELETE CASCADE,
                                             leave_text TEXT NOT NULL,
                                             start_date DATE NOT NULL,
                                             end_date DATE NOT NULL,
                                             status TEXT DEFAULT 'PENDING' NOT NULL,
                                             reason TEXT NOT NULL,
                                             created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                             CHECK (end_date > start_date)
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
                                             notification_id SERIAL PRIMARY KEY,
                                             user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id)  ON DELETE CASCADE,
                                             title VARCHAR(255) NOT NULL,
                                             description TEXT NOT NULL,
                                             created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                             expires_at TIMESTAMP NOT NULL
);

-- Performance Evaluation Table
CREATE TABLE IF NOT EXISTS performance_evaluation (
                                                      performance_evaluation_id SERIAL PRIMARY KEY,
                                                      from_user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id) ON DELETE CASCADE,
                                                      to_user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id) ON DELETE CASCADE,
                                                      comment TEXT NOT NULL,
                                                      created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                                      CHECK (from_user_tenant_id <> to_user_tenant_id)
);

ALTER TABLE contract ALTER COLUMN terms DROP NOT NULL;

CREATE TABLE IF NOT EXISTS evaluation_form (
                                               id SERIAL PRIMARY KEY,
                                               from_user_tenant_id INTEGER NOT NULL,
                                               to_user_tenant_id INTEGER NOT NULL,
                                               status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS evaluation_question (
                                                   id SERIAL PRIMARY KEY,
                                                   question_text VARCHAR(500) NOT NULL,
    form_id INTEGER REFERENCES evaluation_form(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS evaluation_answer (
                                                 id SERIAL PRIMARY KEY,
                                                 form_id INTEGER NOT NULL REFERENCES evaluation_form(id) ON DELETE CASCADE,
    question_id INTEGER NOT NULL REFERENCES evaluation_question(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL,
    comment VARCHAR(1000)
    );


INSERT INTO department (name, description) VALUES
('Human Resources', 'Responsible for employee recruitment, retention, and wellbeing.'),
('Engineering', 'Handles product development and technical infrastructure.'),
('Sales', 'Focuses on selling products and services to customers.'),
('Marketing', 'Promotes the company’s products and builds brand awareness.'),
('Finance', 'Manages budgets, forecasts, and financial planning.'),
('Customer Support', 'Assists customers with issues and questions.'),
('Research and Development', 'Works on innovation and new product design.'),
('Operations', 'Ensures efficient day-to-day business processes.'),
('Legal', 'Handles legal affairs and compliance.'),
('IT', 'Maintains internal technology systems and cybersecurity.');





