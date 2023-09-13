ALTER TABLE booking
    DROP COLUMN IF EXISTS flight_id,
    DROP COLUMN IF EXISTS category_id;

ALTER TABLE booking
    ADD COLUMN IF NOT EXISTS create_time TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN IF NOT EXISTS status_id BIGINT,
    ADD COLUMN IF NOT EXISTS flight_seat_id BIGINT,
    ADD COLUMN IF NOT EXISTS booking_status VARCHAR(255);


DO $$
    BEGIN
        IF NOT EXISTS(SELECT 1
                      FROM information_schema.table_constraints
                      WHERE table_name = 'booking'
                        AND constraint_name = 'fk_flight_seat') THEN
            ALTER TABLE booking
                ADD CONSTRAINT fk_flight_seat FOREIGN KEY (flight_seat_id) REFERENCES flight_seats (id);
        END IF;
    END
$$;