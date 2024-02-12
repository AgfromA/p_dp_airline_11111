-- changeset maxim-zhuravlev:1.25.1
ALTER TABLE booking DROP CONSTRAINT UC_BOOKINGBOOKING_NUMBER_COL;
ALTER TABLE booking DROP COLUMN create_time;
ALTER TABLE booking DROP COLUMN booking_number;
ALTER TABLE booking DROP COLUMN status_id;
