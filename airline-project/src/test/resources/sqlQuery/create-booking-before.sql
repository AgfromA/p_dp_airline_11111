
-- CREATE AIRCRAFTS
insert into aircrafts(id, aircraft_number, model, model_year, flight_range)
values (1, '17000012', 'Embraer E170STD', 2002, 3800),
       (2, '5134', 'Airbus A320-200', 2011, 4300);


-- CREATE CATEGORY
INSERT INTO category(id, category_type)
VALUES (1, 'FIRST');
INSERT INTO category(id, category_type)
VALUES (2, 'BUSINESS');
INSERT INTO category(id, category_type)
VALUES (3, 'PREMIUM_ECONOMY');
INSERT INTO category(id, category_type)
VALUES (4, 'ECONOMY');


-- CREATE FLIGHTS
INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'MSKOMSK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Омск'));
INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (2, 'MSKVLG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DELAYED',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Волгоград'));

-- CREATE SEATS
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
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (7, '2A', true, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (8, '2B', true, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (9, '2C', true, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (10, '2D', true, true,
        (SELECT category.id FROM category WHERE category.id = 4),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (11, '2E', true, true,
        (SELECT category.id FROM category WHERE category.id = 4),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (12, '2F', true, true,
        (SELECT category.id FROM category WHERE category.id = 4),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (13, '11A', false, false,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (14, '21F', false, false,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));


-- CREATE FLIGHT_SEATS
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id, is_booked)
VALUES (1, 500, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 1), true);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id, is_booked)
VALUES (2, 600, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 2), true);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id, is_booked)
VALUES (3, 650, true, true, (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 3), true);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id, is_booked)
VALUES (4, 500, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 4), true);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id, is_booked)
VALUES (5, 500, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 5), false);

-- CREATE DESTINATION
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (1, 'VKO', 'Внуково', 'Москва', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (4, 'OMS', 'Омск', 'Омск', 'Россия', 'GMT +6');

-- CREATE PASSENGERS
INSERT INTO passengers (id, first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES (1, 'John20', 'Simons20', 'J20', TO_DATE('2003/11/08', 'YYYY/MM/DD'), 'MALE', 'passenger20@mail.ru', '79111881111',
        '0011 001800', TO_DATE('2002/01/10', 'YYYY/MM/DD'), 'Россия');

INSERT INTO passengers (id, first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES (2, 'John20', 'Simons20', 'J20', TO_DATE('2020/10/09', 'YYYY/MM/DD'), 'MALE', 'passenger2020@mail.ru', '79111982222',
        '0045 001850', TO_DATE('2022/06/22', 'YYYY/MM/DD'), 'Россия');

INSERT INTO passengers (id, first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES (3, 'Eric', 'Cartman', 'EC', TO_DATE('2000/01/01', 'YYYY/MM/DD'), 'MALE', 'cartman@mail.ru', '79112568888',
        '0046 001946', TO_DATE('2020/03/24', 'YYYY/MM/DD'), 'Россия');

INSERT INTO passengers (id, first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES (4, 'John20', 'Cartman', 'JC', TO_DATE('2014/11/11', 'YYYY/MM/DD'), 'MALE', 'passengerJohn@mail.ru', '79212562666',
        '0044 002187', TO_DATE('2023/011/11', 'YYYY/MM/DD'), 'Россия');

-- CREATE BOOKING
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (1, '2024-02-05 11:51:26.444777', 1, 1,  'NOT_PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (2, '2024-02-05 11:51:26.444777', 2, 2, 'NOT_PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (3, '2024-02-05 11:51:26.444777', 3, 3, 'NOT_PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (4, '2024-02-05 11:51:26.444777', 4, 4, 'NOT_PAID');
INSERT INTO booking(id, booking_data_time, passenger_id, flight_seat_id, booking_status)
VALUES (5, '2024-02-05 11:51:26.444777', 4, 5, 'NOT_PAID');