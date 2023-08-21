CREATE TABLE test_table
(
    id            BIGSERIAL PRIMARY KEY,
    first_name               VARCHAR(128)    NOT NULL,
    last_name                VARCHAR(128)    NOT NULL,
    email                     VARCHAR(255)   NOT NULL,
    password                  VARCHAR(255)   NOT NULL
);