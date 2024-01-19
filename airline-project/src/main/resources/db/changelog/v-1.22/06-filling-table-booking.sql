INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00001', TO_DATE('2022/11/24', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00002', TO_DATE('2022/11/25', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 2));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00003', TO_DATE('2022/11/25', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 3));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00004', TO_DATE('2022/11/25', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 4));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00005', TO_DATE('2022/11/26', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 5));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00006', TO_DATE('2022/11/27', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 6));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00007', TO_DATE('2022/11/27', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 7));
INSERT INTO booking (booking_number, booking_data_time, passenger_id)
VALUES ('BK-00008', TO_DATE('2022/11/28', 'YYYY/MM/DD'),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 8));