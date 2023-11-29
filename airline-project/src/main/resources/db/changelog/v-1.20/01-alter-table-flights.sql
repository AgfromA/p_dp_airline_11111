-- changeset ne_andrey:1.20.1

ALTER TABLE IF EXISTS flights
    RENAME COLUMN departure_date TO arrival_date_new;
ALTER TABLE IF EXISTS flights
    RENAME COLUMN arrival_date TO departure_date;
ALTER TABLE IF EXISTS flights
    RENAME COLUMN arrival_date_new TO arrival_date;


