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

