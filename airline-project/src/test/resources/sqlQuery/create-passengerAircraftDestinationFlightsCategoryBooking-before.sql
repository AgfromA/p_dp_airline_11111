
INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('John2', 'Simons2', 'J2', TO_DATE('2003/11/08', 'YYYY/MM/DD'), 'MALE', 'passenger2@mail.ru', '79111181111',
        '0010 001000', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 1002);

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Пётр2', 'Петров2', 'Петрович2', TO_DATE('1986/01/11', 'YYYY/MM/DD'), 'MALE', 'petrov2@mail.ru', '79111511111',
        '1121 111121', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 1001);


INSERT INTO booking (id,  booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (6001,  NOW(),
           -- for Get
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1001),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 1),

        'NOT_PAID'),
       -- for Edit
       (6002, NOW(),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1002),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 2),

        'NOT_PAID'),
       -- for Delete
       (6003, NOW(),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1002),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 2),

        'NOT_PAID'),
        -- for Edit 2
       (6004,  NOW(),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1001),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 5),

        'NOT_PAID');