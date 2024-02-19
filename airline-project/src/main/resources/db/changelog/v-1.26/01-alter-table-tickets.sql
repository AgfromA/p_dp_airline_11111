ALTER TABLE tickets
DROP COLUMN IF EXISTS flight_id;

ALTER TABLE tickets
    ADD COLUMN IF NOT EXISTS booking_id BIGINT;

DELETE FROM tickets;

ALTER TABLE tickets ADD CONSTRAINT fk_passenger FOREIGN KEY (passenger_id) REFERENCES passengers (id);
ALTER TABLE tickets ADD CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking (id);