-- changeset maxim-zhuravlev:1.25.2

UPDATE booking SET flight_seat_id = 22 WHERE id = 1;
UPDATE booking SET flight_seat_id = 43 WHERE id = 2;
UPDATE booking SET flight_seat_id = 3 WHERE id = 3;
UPDATE booking SET flight_seat_id = 51 WHERE id = 4;
UPDATE booking SET flight_seat_id = 13 WHERE id = 5;
UPDATE booking SET flight_seat_id = 16 WHERE id = 6;
UPDATE booking SET flight_seat_id = 4 WHERE id = 7;
UPDATE booking SET flight_seat_id = 8 WHERE id = 8;
UPDATE booking SET flight_seat_id = 9 WHERE id = 9;
UPDATE booking SET flight_seat_id = 25 WHERE id = 10;

UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 1;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 2;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 3;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 4;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 5;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 6;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 7;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 8;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 9;
UPDATE booking SET booking_status = 'NOT_PAID' WHERE id = 10;

UPDATE booking SET passenger_id = 14 WHERE id = 1;
UPDATE booking SET passenger_id = 15 WHERE id = 2;
UPDATE booking SET passenger_id = 16 WHERE id = 3;
UPDATE booking SET passenger_id = 17 WHERE id = 4;
UPDATE booking SET passenger_id = 18 WHERE id = 5;
UPDATE booking SET passenger_id = 19 WHERE id = 6;
UPDATE booking SET passenger_id = 20 WHERE id = 7;
UPDATE booking SET passenger_id = 21 WHERE id = 8;
UPDATE booking SET passenger_id = 14 WHERE id = 9;
UPDATE booking SET passenger_id = 15 WHERE id = 10;

ALTER TABLE booking ADD CONSTRAINT fk_passenger FOREIGN KEY (passenger_id) REFERENCES passengers (id);
