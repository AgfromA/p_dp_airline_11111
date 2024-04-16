insert into destination (id, airport_code, airport_name, city_name, timezone, country_name, is_deleted) values (2, 'ABA', 'Абакан', 'Абакан', '+3', 'Россия', false);
insert into destination (id, airport_code, airport_name, city_name, timezone, country_name, is_deleted) values (3, 'ADH', 'Алдан', 'Алдан', '+3', 'Россия', false);
insert into destination (id, airport_code, airport_name, city_name, timezone, country_name, is_deleted) values (4, 'SVO', 'Шереметьево', 'Москва', '+3', 'Россия', false);

insert into destination (id, airport_code, airport_name, city_name, timezone, country_name, is_deleted) values (5, 'KMW', 'Кострома', 'Кострома', '+3', 'Россия', false);

INSERT INTO category (id, category_type) VALUES (1, 'FIRST'),
                                                (2, 'BUSINESS'),
                                                (3, 'PREMIUM_ECONOMY'),
                                                (4, 'ECONOMY');

INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (1, '5134', 'Airbus A320-200', 2011, 4300);

INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'KMWSVO', '2022-11-23 07:30:00', '2022-11-23 04:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1),
        (SELECT destination.id FROM destination WHERE city_name = 'Кострома'),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'));

INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (1, '1A', false, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1));

INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('John2', 'Simons2', 'J2', TO_DATE('2003/11/08', 'YYYY/MM/DD'), 'MALE', 'passenger2@mail.ru', '79111181111',
        '0010 001000', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 1);

INSERT INTO flight_seats (id, fare, is_registered, is_sold, flight_id, seat_id)
VALUES (1, 500, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 1));