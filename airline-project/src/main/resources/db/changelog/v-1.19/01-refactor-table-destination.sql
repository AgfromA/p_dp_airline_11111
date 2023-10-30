-- changeset yuri_solom:1.19.1

ALTER TABLE destination
    ALTER COLUMN airport_name SET DATA TYPE VARCHAR(45);

ALTER TABLE destination
    ALTER COLUMN city_name SET DATA TYPE VARCHAR(45);