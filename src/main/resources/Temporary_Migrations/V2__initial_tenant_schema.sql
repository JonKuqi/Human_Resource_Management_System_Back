
-- User Tenant Table
CREATE TABLE IF NOT EXISTS user_tenant (
                                           user_tenant_id SERIAL PRIMARY KEY,
                                           user_id INT NOT NULL REFERENCES public.user(user_id),
                                           tenant_id INT NOT NULL REFERENCES public.tenant(tenant_id),
                                           first_name VARCHAR(50) NOT NULL,
                                           last_name VARCHAR(50) NOT NULL,
                                           phone VARCHAR(20) NOT NULL,
                                           gender VARCHAR(20),
                                           profile_photo BYTEA,
                                           address_id INT NOT NULL REFERENCES public.address(address_id),
                                           created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Application Table
CREATE TABLE IF NOT EXISTS application (
                                           application_id SERIAL PRIMARY KEY,
                                           job_listing_id INT NOT NULL REFERENCES public.job_listing(job_listing_id),
                                           applicant_name VARCHAR(100) NOT NULL,
                                           applicant_email VARCHAR(255) NOT NULL,
                                           applicant_gender VARCHAR(20),
                                           applicant_birth_date DATE,
                                           applicant_phone VARCHAR(20) NOT NULL,
                                           experience TEXT,
                                           applicant_comment TEXT,
                                           cv BYTEA,
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
                                         user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
                                         role_id INT NOT NULL REFERENCES role(role_id),
                                         UNIQUE(user_tenant_id, role_id)
);

-- Permission Table
CREATE TABLE IF NOT EXISTS permission (
                                          permission_id SERIAL PRIMARY KEY,
                                          name VARCHAR(100) UNIQUE NOT NULL,
                                          description TEXT NOT NULL
);

-- Role Permission Junction Table
CREATE TABLE IF NOT EXISTS role_permission (
                                               role_permission_id SERIAL PRIMARY KEY,
                                               role_id INT NOT NULL REFERENCES role(role_id),
                                               permission_id INT NOT NULL REFERENCES permission(permission_id),
                                               UNIQUE(role_id, permission_id)
);

-- Position Table
CREATE TABLE IF NOT EXISTS position (
                                        position_id SERIAL PRIMARY KEY,
                                        department_id INT NOT NULL REFERENCES department(department_id),
                                        title VARCHAR(100) NOT NULL,
                                        description TEXT,
                                        created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

-- Contract Table
CREATE TABLE IF NOT EXISTS contract (
                                        contract_id SERIAL PRIMARY KEY,
                                        user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
                                        position_id INT NOT NULL REFERENCES position(position_id),
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
                                       user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
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
                                             user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
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
                                             user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
                                             title VARCHAR(255) NOT NULL,
                                             description TEXT NOT NULL,
                                             created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                             expires_at TIMESTAMP NOT NULL
);

-- Performance Evaluation Table
CREATE TABLE IF NOT EXISTS performance_evaluation (
                                                      performance_evaluation_id SERIAL PRIMARY KEY,
                                                      from_user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
                                                      to_user_tenant_id INT NOT NULL REFERENCES user_tenant(user_tenant_id),
                                                      comment TEXT NOT NULL,
                                                      created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                                      CHECK (from_user_tenant_id <> to_user_tenant_id)
);

