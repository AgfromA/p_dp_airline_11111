-- Создаем 4 аэропорта

INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (1, 'VKO', 'Внуково', 'Москва', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (2, 'OMS', 'Омск', 'Омск', 'Россия', 'GMT +6');

-- Создаем категории мест

INSERT INTO category (id, category_type)
VALUES (1, 'FIRST');
INSERT INTO category (id, category_type)
VALUES (2, 'BUSINESS');
INSERT INTO category (id, category_type)
VALUES (3, 'PREMIUM_ECONOMY');
INSERT INTO category (id, category_type)
VALUES (4, 'ECONOMY');

-- Создаем 4 aircrafts

INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (1, '17000012', 'Embraer E170STD', 2002, 3800);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (2, '5134', 'Airbus A320-200', 2011, 4300);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (3, '35283', 'Boeing 737-800', 2008, 5765);
INSERT INTO aircrafts (id, aircraft_number, model, model_year, flight_range)
VALUES (4, '31334', 'Boeing 737-900ER', 2003, 5084);

-- Создаем 3 места в aircrafts.id = 1

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

-- Создаем 3 места в aircrafts.id = 2

INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (4, '2A', false, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (5, '2B', false, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (6, '2C', false, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2));

-- Создаем 3 места в aircrafts.id = 3

INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (7, '3A', true, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (8, '3B', true, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (9, '3C', true, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3));

-- Создаем 3 места в aircrafts.id = 4

INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (10, '4A', true, true,
        (SELECT category.id FROM category WHERE category.id = 1),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (11, '4B', true, true,
        (SELECT category.id FROM category WHERE category.id = 2),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4));
INSERT INTO seats (id, seat_number, is_near_emergency_exit, is_locked_back, category_id, aircraft_id)
VALUES (12, '4C', true, true,
        (SELECT category.id FROM category WHERE category.id = 3),
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4));


-- Прямые Рейсы туда: Внуково-Омск
-- Прямые рейсы обратно: Омск-Внуково


-- 1. В базе: один прямой рейс туда с наличием мест (3 свободных).
--    Поиск: рейс туда (2023-04-01) без поиска обратного рейса
-- 2. В базе: один прямой рейс туда и один прямой рейс обратно с наличием мест (3 свободных).
--    Поиск: рейс туда (2023-04-01) и рейс обратно (2023-04-03)

-- ТУДА (для 1-2 тестов)

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (1, 'VKOOMS', '2023-04-01 12:40:00', '2023-04-01 13:40:00', 'ON_TIME',
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
VALUES (3, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 1),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- ОБРАТНО (для 2 теста)

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (2, 'OMSVKO', '2023-04-03 12:40:00', '2023-04-03 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (4, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (5, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (6, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 2),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


-- 3. В базе: два прямых рейсов туда и два прямых рейсов обратно (туда и обратно - 3 свободных мест).
-- Поиск: рейс туда (2023-03-01) и рейс обратно (2023-04-06)

-- ТУДА №1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (62, 'VKOOMS', '2023-03-01 12:40:00', '2023-03-01 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (184, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 62),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (185, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 62),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (186, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 62),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- ТУДА №2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (63, 'VKOOMS', '2023-03-01 18:00:00', '2023-03-01 19:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (187, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 63),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (188, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 63),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (189, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 63),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- ОБРАТНО №1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (6, 'OMSVKO', '2023-04-06 12:40:00', '2023-04-06 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (16, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 6),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (17, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 6),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (18, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 6),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- ОБРАТНО №2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (56, 'OMSVKO', '2023-04-06 21:00:00', '2023-04-06 22:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (166, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 56),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (167, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 56),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (168, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 56),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- 4. В базе: два прямых рейсы туда и два прямых
--    рейсы обратно, все с наличием мест. Поиск: рейс туда (2023-04-20), рейс обратно (2023-04-25)

-- ТУДА
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (14, 'VKOOMS', '2023-04-20 12:40:00', '2023-04-20 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (40, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 14),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (41, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 14),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (42, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 14),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (15, 'VKOOMS', '2023-04-20 18:00:00', '2023-04-20 19:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (43, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 15),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (44, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 15),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (45, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 15),
        (SELECT seats.id FROM seats WHERE seats.id = 3));


-- РЕЙСЫ ОБРАТНО
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (20, 'OMSVKO', '2023-04-25 01:00:00', '2023-04-25 02:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (58, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 20),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (59, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 20),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (60, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 20),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (21, 'OMSVKO', '2023-04-25 12:40:00', '2023-04-25 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (61, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (62, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (63, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

--________________________________________________________________________________________________________________
--________________________________________________________________________________________________________________


-- ТЕСТИРОВАНИЕ ПРИ НАЛИЧИИ МЕСТ ТОЛЬКО В РЕЙСАХ ТУДА (туда используются рейсы, уже имеющиеся в БД)

-- 5. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 3 , обратно - 0 свободных мест).
-- Поиск: рейс туда (2023-04-01) и рейс обратно (2023-05-03)

-- ОБРАТНО

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (26, 'OMSVKO', '2023-05-03 12:40:00', '2023-05-03 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (76, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 26),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (77, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 26),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (78, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 26),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------

-- Рейсы без вторых стыковочных рейсов. Не должны находиться
-- (Внуково-Петрозаводск и Ставрополь-Омск)
-- На 2023-04-06 и на 2023-04-20

INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (5, 'PES', 'Петрозаводск', 'Петрозаводск', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (6, 'STW', 'Ставрополь', 'Ставрополь', 'Россия', 'GMT +6');


INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (72, 'VKOPES', '2023-04-06 05:00:00', '2023-04-06 06:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 1, 5);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (214, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 72),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (215, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 72),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (216, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 72),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (73, 'VKOPES', '2023-04-20 05:00:00', '2023-04-20 06:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 1, 5);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (217, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 73),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (218, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 73),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (219, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 73),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (74, 'STWOMS', '2023-04-06 05:00:00', '2023-04-06 06:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 6, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (220, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 74),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (221, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 74),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (222, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 74),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (75, 'STWOMS', '2023-04-20 05:00:00', '2023-04-20 06:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 6, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (223, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 75),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (224, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 75),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (225, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 75),
        (SELECT seats.id FROM seats WHERE seats.id = 6));