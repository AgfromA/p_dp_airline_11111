-- changeset maxim-zhuravlev:1.28.2
ALTER TABLE tickets ADD CONSTRAINT fk_passenger FOREIGN KEY (passenger_id) REFERENCES passengers (id);
ALTER TABLE tickets ADD CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES booking (id);
