DO
$$
    BEGIN
        IF EXISTS(SELECT COLUMN_NAME
                  FROM information_schema.columns
                  WHERE table_name = 'flight_seats'
                    AND column_name = 'is_booking') THEN
            ALTER TABLE flight_seats
                RENAME COLUMN is_booking TO is_booked;
        end if;
    END
$$
