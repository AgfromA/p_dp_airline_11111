
ALTER TABLE account
ADD COLUMN username VARCHAR(255);

UPDATE account SET username = 'admin' WHERE account_id = 1;
UPDATE account SET username = 'user' WHERE account_id = 2;

ALTER TABLE account ALTER COLUMN username SET NOT NULL;
ALTER TABLE account ADD CONSTRAINT unique_username UNIQUE (username);
