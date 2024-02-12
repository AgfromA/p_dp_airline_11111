INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (1, 'VKO', 'Внуково', 'Москва', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (2, 'OMS', 'Омск', 'Омск', 'Россия', 'GMT +6');


INSERT INTO category (id, category_type)
VALUES (1, 'FIRST');
INSERT INTO category (id, category_type)
VALUES (2, 'BUSINESS');
INSERT INTO category (id, category_type)
VALUES (3, 'PREMIUM_ECONOMY');
INSERT INTO category (id, category_type)
VALUES (4, 'ECONOMY');


INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (1, '17000012', 'Embraer E170STD', 2002, 3800);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (2, '5134', 'Airbus A320-200', 2011, 4300);


INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'VKOOMS', '2023-04-01 11:20:00', '2023-04-01 17:50:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (2, 'OMSVKO', '2023-04-03 07:05:00', '2023-04-03 07:55:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);

INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (1, '1A', false, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (2, '1B', false, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (3, '1C', false, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (4, '1D', false, true,
        (SELECT category.id FROM category WHERE category.id = 4),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (5, '1E', false, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (6, '1F', false, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));


INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (1, 500, true, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (2, 600, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (3, 650, true, true, (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 3));


INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('John1', 'Simons1', 'J1', TO_DATE('2002/11/05', 'YYYY/MM/DD'), 'MALE', 'passenger21@mail.ru', '79111183111',
        '0010 001040', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 1);

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Пётр22', 'Петров22', 'Петрович22', TO_DATE('1986/01/11', 'YYYY/MM/DD'), 'MALE', 'petrov22@mail.ru',
        '79111511111',
        '1121 113121', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 2);

INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Pavel100', 'Pavlov100', 'Pavlovich100', TO_DATE('2000/11/08', 'YYYY/MM/DD'), 'MALE',
        'passengerpavlov100@mail.ru', '79121881111',
        '0011 001800', TO_DATE('2004/01/10', 'YYYY/MM/DD'), 'Россия', 3);

INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (1, '2024-02-05 11:51:26.444777',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 1),
        'PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (2, '2024-02-05 11:51:26.444777',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 2),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 2),
        'PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (3, '2024-02-05 11:51:26.444777',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 3),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 3),
        'PAID');

INSERT INTO tickets (id, ticket_number, passenger_id, flight_seat_id, booking_id)
VALUES (2, 'SD-2222',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 2),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 2),
        (SELECT booking.id FROM booking WHERE booking.id = 2));

INSERT INTO tickets (id, ticket_number, passenger_id, flight_seat_id, booking_id)
VALUES (3, 'ZX-3333',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 3),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 3),
        (SELECT booking.id FROM booking WHERE booking.id = 3));

--for update
INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Merf', 'Merfov', 'Merfovich', TO_DATE('2006/11/08', 'YYYY/MM/DD'), 'MALE',
        'merfcat@mail.ru', '79121881111',
        '0011 001860', TO_DATE('2008/01/10', 'YYYY/MM/DD'), 'Россия', 4);

INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (4, 650, true, false, (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 4));

--not paid
INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Gosha200', 'Goshanov', 'Goshanovich', TO_DATE('1996/10/08', 'YYYY/MM/DD'), 'MALE',
        'goshachelovek@mail.ru', '79121881111',
        '0011 001850', TO_DATE('2006/01/10', 'YYYY/MM/DD'), 'Россия', 5);

INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (5, 650, true, false, (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 5));

INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (5, '2024-02-05 11:51:26.444777',
        (SELECT passengers.id FROM passengers WHERE passengers.id = 5),
        (SELECT flight_seats.id FROM flight_seats WHERE flight_seats.id = 5),
        'NOT_PAID');