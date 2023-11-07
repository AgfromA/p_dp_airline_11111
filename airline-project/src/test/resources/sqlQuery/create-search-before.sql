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
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (3, '35283', 'Boeing 737-800', 2008, 5765);





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








INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'MSKOMSK', '2023-04-01 12:40:00', '2023-04-01 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (1, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 1));

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (2, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 2));

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (3, 650, false, false, false, (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 3));







INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (2, 'OMSKMSK', '2023-04-05 12:40:00', '2023-04-05 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (4, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 5));

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (5, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (6, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 7));