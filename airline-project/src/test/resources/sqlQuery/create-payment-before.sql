INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('Пётр2', 'Петров2', 'Петрович2', TO_DATE('1986/01/11', 'YYYY/MM/DD'), 'MALE', 'petrov2@mail.ru', '79111511111',
        '1121 111121', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия', 1001);

INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country, id)
VALUES ('John20', 'Simons20', 'J20', TO_DATE('2003/11/08', 'YYYY/MM/DD'), 'MALE', 'passenger20@mail.ru', '79111881111',
        '0011 001800', TO_DATE('2002/01/10', 'YYYY/MM/DD'), 'Россия', 1002);


INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (2001, '0000002001', 'Embraer E170STD', 2002, 3800),
       (2002, '0000002002', 'Airbus A320-200', 2011, 4300);


INSERT INTO destination (id, airport_code, airport_name, city_name, timezone, country_name)
VALUES (3001, 'SVO', 'Шереметьево', 'Москва', '+3', 'Россия'),
       (3002, 'VOG', 'Гумрак', 'Волгоград', '+3', 'Россия');


INSERT INTO flights (id, code, arrival_date, departure_date, flight_status, aircraft_id, from_id, to_id)
VALUES (4001, 'MSKVOG', NOW(), NOW(), 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2001),
        (SELECT destination.id FROM destination WHERE city_name = 'Москва'),
        (SELECT destination.id FROM destination WHERE city_name = 'Волгоград')),
       (4002, 'VOGUFA', NOW(), NOW(), 'DELAYED',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2002),
        (SELECT destination.id FROM destination WHERE city_name = 'Волгоград'),
        (SELECT destination.id FROM destination WHERE city_name = 'Уфа'));


insert into category(id, category_type)
values (5001, 'BUSINESS'),
       (5002, 'ECONOMY');


INSERT INTO booking (id,  booking_data_time, passenger_id)
VALUES (6001, NOW(),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1001)),

       (6002, NOW(),
        (SELECT passengers.id FROM passengers WHERE passengers.id = 1002));



INSERT INTO payments (id, payment_state) VALUES (3001, 'CREATED');


INSERT INTO payments_bookings_id (payments_id, index, bookings_id) VALUES (3001, 0, 6001), (3001, 1, 6002);