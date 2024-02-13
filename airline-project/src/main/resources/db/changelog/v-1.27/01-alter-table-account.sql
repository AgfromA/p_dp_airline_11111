
ALTER TABLE account
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN last_name SET NOT NULL,
    ALTER COLUMN birth_date SET NOT NULL,
    ALTER COLUMN phone_number SET NOT NULL,
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN password SET NOT NULL,
    ALTER COLUMN security_question SET NOT NULL,
    ALTER COLUMN answer_question SET NOT NULL;


ALTER TABLE account
    ADD CONSTRAINT check_first_name_length CHECK (LENGTH(first_name) >= 2 AND LENGTH(first_name) <= 128),
    ADD CONSTRAINT check_last_name_length CHECK (LENGTH(last_name) >= 2 AND LENGTH(last_name) <= 128),
    ADD CONSTRAINT check_birth_date_past CHECK (birth_date <= CURRENT_DATE),
    ADD CONSTRAINT unique_email_constraint_account UNIQUE (email),
    ADD CONSTRAINT check_phone_number_length CHECK (LENGTH(phone_number) >= 6 AND LENGTH(phone_number) <= 64),
    ADD CONSTRAINT check_security_question_length CHECK (LENGTH(security_question) <= 256);
