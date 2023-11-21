-- Создаем 4 аэропорта

INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (1, 'VKO', 'Внуково', 'Москва', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (2, 'OMS', 'Омск', 'Омск', 'Россия', 'GMT +6');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (3, 'KZN', 'Казань', 'Казань', 'Россия', 'GMT +3');
INSERT INTO destination (id, airport_code, airport_name, city_name, country_name, timezone)
VALUES (4, 'KUF', 'Курумоч', 'Самара', 'Россия', 'GMT +4');

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
-- Непрямые рейсы туда: Внуково-Курумоч - Курумоч-Омск, Внуково-Казань - Казань-Омск
-- Непрямые рейсы обратно: Омск-Курумоч - Курумоч-Внуково, Омск-Казань - Казань-Внуково


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


-- 3. В базе: один прямой и один непрямой рейс туда с наличием мест (3 свободных).
--    Поиск: рейс туда (2023-04-05) без поиска обратного рейса
-- 4. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
--    с наличием мест (3 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-06)
-- 5. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс
--    с наличием мест (3 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-07)
-- 6. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой
--    рейс обратно с наличием мест (3 свободных).
--    Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-08))

-- ТУДА (для всех 3-6 тестов)
-- ПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (3, 'VKOOMS', '2023-04-05 12:40:00', '2023-04-05 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (7, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 3),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (8, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 3),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (9, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 3),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (4, 'VKOKZN', '2023-04-05 05:30:00', '2023-04-05 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (10, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 4),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (11, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 4),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (12, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 4),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (5, 'KZNOMS', '2023-04-05 09:30:00', '2023-04-05 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (13, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 5),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (14, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 5),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (15, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 5),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- ОБРАТНО (для 4 теста)

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

-- ОБРАТНО (для 5 теста)

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (7, 'OMSKZN', '2023-04-07 02:30:00', '2023-04-07 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (19, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 7),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (20, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 7),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (21, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 7),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (8, 'KZNVKO', '2023-04-07 06:45:00', '2023-04-07 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (22, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 8),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (23, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 8),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (24, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 8),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- ОБРАТНО (для 6 теста)
-- ПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (9, 'OMSVKO', '2023-04-08 12:40:00', '2023-04-08 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (25, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 9),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (26, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 9),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (27, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 9),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (10, 'OMSKZN', '2023-04-08 02:30:00', '2023-04-08 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (28, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 10),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (29, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 10),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (30, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 10),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (11, 'KZNVKO', '2023-04-08 06:45:00', '2023-04-08 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (31, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 11),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (32, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 11),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (33, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 11),
        (SELECT seats.id FROM seats WHERE seats.id = 9));


-- 7. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы,
--    все - с наличием мест. Поиск: рейс туда (2023-04-01), рейс обратно (2023-04-08)

-- ИСПОЛЬЗУЮТСЯ РЕЙСЫ ТУДА И ОБРАТНО ИЗ БД В СООТВЕТСТВУЮЩИЕ ДНИ


-- 8. В базе: туда только один НЕпрямой рейс, обратно - один прямой и один непрямой рейсы,
--    все - с наличием мест. Поиск: рейс туда (2023-04-06), рейс обратно (2023-04-08)

-- ТУДА
-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (12, 'VKOKZN', '2023-04-06 05:30:00', '2023-04-06 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (34, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 12),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (35, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 12),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (36, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 12),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (13, 'KZNOMS', '2023-04-06 09:30:00', '2023-04-06 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (37, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 13),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (38, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 13),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (39, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 13),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- Обратно используются рейсы от (2023-04-08)


-- 9. В базе: два прямых и два непрямых рейсы туда и два прямых и два непрямых
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

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (16, 'VKOKZN', '2023-04-20 05:30:00', '2023-04-20 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (46, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 16),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (47, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 16),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (48, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 16),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (17, 'KZNOMS', '2023-04-20 09:30:00', '2023-04-20 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (49, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 17),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (50, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 17),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (51, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 17),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (18, 'VKOKUF', '2023-04-20 12:00:00', '2023-04-20 13:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 1, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (52, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 18),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (53, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 18),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (54, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 18),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (19, 'KUFOMS', '2023-04-20 16:30:00', '2023-04-20 18:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (55, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 19),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (56, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 19),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (57, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 19),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

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
VALUES (61, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (62, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (63, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 21),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (22, 'OMSKZN', '2023-04-25 02:30:00', '2023-04-25 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (64, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 22),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (65, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 22),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (66, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 22),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (23, 'KZNVKO', '2023-04-25 06:45:00', '2023-04-25 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (67, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 23),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (68, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 23),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (69, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 23),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (24, 'OMSKUF', '2023-04-25 11:20:00', '2023-04-25 12:25:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 2, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (70, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 24),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (71, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 24),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (72, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 24),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (25, 'KUFVKO', '2023-04-25 16:10:00', '2023-04-25 17:45:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (73, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 25),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (74, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 25),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (75, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 25),
        (SELECT seats.id FROM seats WHERE seats.id = 12));


--________________________________________________________________________________________________________________
--________________________________________________________________________________________________________________


-- ТЕСТИРОВАНИЕ ПРИ НАЛИЧИИ МЕСТ ТОЛЬКО В РЕЙСАХ ТУДА (туда используются рейсы, уже имеющиеся в БД)


-- 10. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 3 , обратно - 0 свободных мест).
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


-- 11. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
-- (туда - 3 , обратно - 0 свободных мест). Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-06)

-- ОБРАТНО

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (27, 'OMSVKO', '2023-05-06 12:40:00', '2023-05-06 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (79, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 27),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (80, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 27),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (81, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 27),
        (SELECT seats.id FROM seats WHERE seats.id = 6));


-- 12. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс
-- (туда - 3 , обратно 0 свободных мест). Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-07)

-- ОБРАТНО

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (28, 'OMSKZN', '2023-05-07 02:30:00', '2023-05-07 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (82, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 28),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (83, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 28),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (84, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 28),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (29, 'KZNVKO', '2023-05-07 06:45:00', '2023-05-07 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (85, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 29),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (86, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 29),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (87, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 29),
        (SELECT seats.id FROM seats WHERE seats.id = 9));


-- 13. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой рейс
-- обратно (туда - 3 , обратно 0 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-08))

-- ОБРАТНО
-- ПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (30, 'OMSVKO', '2023-05-08 12:40:00', '2023-05-08 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (88, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 30),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (89, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 30),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (90, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 30),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (31, 'OMSKZN', '2023-05-08 02:30:00', '2023-05-08 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (91, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 31),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (92, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 31),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (93, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 31),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (32, 'KZNVKO', '2023-05-08 06:45:00', '2023-05-08 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (94, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 32),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (95, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 32),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (96, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 32),
        (SELECT seats.id FROM seats WHERE seats.id = 9));


-- 14. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы
-- (туда - 3 , обратно - 0 свободных мест). Поиск: рейс туда (2023-04-01), рейс обратно (2023-05-08)

-- ИСПОЛЬЗУЮТСЯ РЕЙСЫ ТУДА И ОБРАТНО ИЗ БД В СООТВЕТСТВУЮЩИЕ ДНИ


-- 15. В базе: туда только один непрямой рейс, обратно - один прямой и один непрямой рейсы
-- (туда - 3 , обратно - 0 свободных мест). Поиск: рейс туда (2023-04-06), рейс обратно (2023-05-08)

-- ИСПОЛЬЗУЮТСЯ РЕЙСЫ ТУДА И ОБРАТНО ИЗ БД В СООТВЕТСТВУЮЩИЕ ДНИ


-- 16. В базе: два прямых и два непрямых рейсы туда и два прямых и два непрямых рейсы
-- (туда - 3 , обратно 0 свободных). Поиск: рейс туда (2023-04-20), рейс обратно (2023-05-25)

-- ОБРАТНО
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (33, 'OMSVKO', '2023-05-25 01:00:00', '2023-05-25 02:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (97, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 33),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (98, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 33),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (99, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 33),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (34, 'OMSVKO', '2023-05-25 12:40:00', '2023-05-25 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (100, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 34),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (101, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 34),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (102, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 34),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (35, 'OMSKZN', '2023-05-25 02:30:00', '2023-05-25 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (103, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 35),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (104, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 35),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (105, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 35),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (36, 'KZNVKO', '2023-05-25 06:45:00', '2023-05-25 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (106, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 36),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (107, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 36),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (108, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 36),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (37, 'OMSKUF', '2023-05-25 11:20:00', '2023-05-25 12:25:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 2, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (109, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 37),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (110, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 37),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (111, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 37),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (38, 'KUFVKO', '2023-05-25 16:10:00', '2023-05-25 17:45:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (112, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 38),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (113, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 38),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (114, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 38),
        (SELECT seats.id FROM seats WHERE seats.id = 12));


--________________________________________________________________________________________________________________
--________________________________________________________________________________________________________________


-- ТЕСТИРОВАНИЕ ПРИ НАЛИЧИИ МЕСТ ТОЛЬКО В ОБРАТНЫХ РЕЙСАХ (обратно используются рейсы, уже имеющиеся в БД)

-- 17. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 0, обратно - 3 свободных мест).
-- Поиск: рейс туда (2023-03-01) и рейс обратно (2023-04-03)

-- ТУДА

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (39, 'VKOOMS', '2023-03-01 12:40:00', '2023-03-01 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (115, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 39),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (116, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 39),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (117, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 39),
        (SELECT seats.id FROM seats WHERE seats.id = 3));


-- 18. В базе: один прямой и один непрямой рейс туда (туда - 0 свободных мест).
-- Поиск: рейс туда (2023-03-05) без поиска обратного рейса

-- 19. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
-- (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-06)

-- 20. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс
-- (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-07)

-- 21. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой рейс
-- обратно (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-08))

-- ТУДА
-- ПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (40, 'VKOOMS', '2023-03-05 12:40:00', '2023-03-05 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (118, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 40),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (119, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 40),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (120, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 40),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (41, 'VKOKZN', '2023-03-05 05:30:00', '2023-03-05 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (121, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 41),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (122, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 41),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (123, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 41),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (42, 'KZNOMS', '2023-03-05 09:30:00', '2023-03-05 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (124, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 42),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (125, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 42),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (126, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 42),
        (SELECT seats.id FROM seats WHERE seats.id = 9));


-- 22. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы
-- (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-01), рейс обратно (2023-04-08)

-- ИСПОЛЬЗУЮТСЯ РЕЙСЫ ТУДА И ОБРАТНО ИЗ БД В СООТВЕТСТВУЮЩИЕ ДНИ


-- 23. В базе: туда только один НЕпрямой рейс, обратно - один прямой и один непрямой рейсы
-- (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-06), рейс обратно (2023-04-08)

-- ТУДА
-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (43, 'VKOKZN', '2023-03-06 05:30:00', '2023-03-06 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (127, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 43),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (128, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 43),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (129, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 43),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (44, 'KZNOMS', '2023-03-06 09:30:00', '2023-03-06 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (130, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 44),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (131, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 44),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (132, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 44),
        (SELECT seats.id FROM seats WHERE seats.id = 9));


-- 24. В базе: два прямых и два непрямых рейсы туда и два прямых и два непрямых рейсы
-- обратно (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-20), рейс обратно (2023-04-25)

-- РЕЙСЫ ТУДА
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (45, 'VKOOMS', '2023-03-20 12:40:00', '2023-03-20 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (133, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 45),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (134, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 45),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (135, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 45),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (46, 'VKOOMS', '2023-03-20 18:00:00', '2023-03-20 19:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (136, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 46),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (137, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 46),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (138, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 46),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (47, 'VKOKZN', '2023-03-20 05:30:00', '2023-03-20 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (139, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 47),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (140, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 47),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (141, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 47),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (48, 'KZNOMS', '2023-03-20 09:30:00', '2023-03-20 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (142, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 48),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (143, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 48),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (144, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 48),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (49, 'VKOKUF', '2023-03-20 12:00:00', '2023-03-20 13:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 1, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (145, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 49),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (146, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 49),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (147, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 49),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (50, 'KUFOMS', '2023-03-20 16:30:00', '2023-03-20 18:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (148, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 50),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (149, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 50),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (150, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 50),
        (SELECT seats.id FROM seats WHERE seats.id = 12));


--________________________________________________________________________________________________________________
--________________________________________________________________________________________________________________


-- ТЕСТИРОВАНИЕ ПРИ ОТСУТСТВИИ МЕСТ ВРАЗБРОС НА ПРЯМЫХ И НЕАПЯМЫХ РЕЙСАХ ТУДА И ОБРАТНО


-- 25. В базе: один прямой и два непрямых рейсов туда (места: прямые - 0, непрямые 2 рейса со свободными 3-мя местами)
-- и два прямых и два непрямых рейсов обратно (места: прямые - 2, непрямые 1 рейс со свободными местами (3 места)).
-- Поиск: рейс туда (2023-07-01), рейс обратно (2023-07-05)

-- ТУДА
-- ПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (51, 'VKOOMS', '2023-07-01 12:40:00', '2023-07-01 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 1), 1, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (151, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 51),
        (SELECT seats.id FROM seats WHERE seats.id = 1));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (152, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 51),
        (SELECT seats.id FROM seats WHERE seats.id = 2));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (153, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 51),
        (SELECT seats.id FROM seats WHERE seats.id = 3));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (52, 'VKOKZN', '2023-07-01 05:30:00', '2023-07-01 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (154, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 52),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (155, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 52),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (156, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 52),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (53, 'KZNOMS', '2023-07-01 09:30:00', '2023-07-01 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (157, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 53),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (158, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 53),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (159, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 53),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (54, 'VKOKUF', '2023-07-01 12:00:00', '2023-07-01 13:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 1, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (160, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 54),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (161, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 54),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (162, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 54),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (55, 'KUFOMS', '2023-07-01 16:30:00', '2023-07-01 18:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (163, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 55),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (164, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 55),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (165, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 55),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

-- ОБРАТНО
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (56, 'OMSVKO', '2023-07-05 01:00:00', '2023-07-05 02:00:00', 'ON_TIME',
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

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (57, 'OMSVKO', '2023-07-05 12:40:00', '2023-07-05 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (169, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 57),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (170, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 57),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (171, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 57),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (58, 'OMSKZN', '2023-07-05 02:30:00', '2023-07-05 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (172, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 58),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (173, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 58),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (174, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 58),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (59, 'KZNVKO', '2023-07-05 06:45:00', '2023-07-05 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (175, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 59),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (176, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 59),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (177, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 59),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (60, 'OMSKUF', '2023-07-05 11:20:00', '2023-07-05 12:25:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 2, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (178, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 60),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (179, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 60),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (180, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 60),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (61, 'KUFVKO', '2023-07-05 16:10:00', '2023-07-05 17:45:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (181, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 61),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (182, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 61),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (183, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 61),
        (SELECT seats.id FROM seats WHERE seats.id = 12));


-- 26. В базе: два прямых и два непрямых рейсов туда (прямые - 2, непрямые 1 рейс со свободными местами (3 места))
-- и два прямых и один непрямой рейсы обратно (прямые - 1, непрямые 0 рейсов со свободными местами (3 места)).
-- Поиск: рейс туда (2023-07-10), рейс обратно (2023-07-15)

-- ТУДА
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (62, 'VKOOMS', '2023-07-10 12:40:00', '2023-07-10 13:40:00', 'ON_TIME',
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

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (63, 'VKOOMS', '2023-07-10 18:00:00', '2023-07-10 19:00:00', 'ON_TIME',
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

-- НЕПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (64, 'VKOKZN', '2023-07-10 05:30:00', '2023-07-10 06:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 1, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (190, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 64),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (191, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 64),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (192, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 64),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (65, 'KZNOMS', '2023-07-10 09:30:00', '2023-07-10 10:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (193, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 65),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (194, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 65),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (195, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 65),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

-- НЕПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (66, 'VKOKUF', '2023-07-10 12:00:00', '2023-07-10 13:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 1, 4);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (196, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 66),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (197, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 66),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (198, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 66),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (67, 'KUFOMS', '2023-07-10 16:30:00', '2023-07-10 18:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 4), 4, 2);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (199, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 67),
        (SELECT seats.id FROM seats WHERE seats.id = 10));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (200, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 67),
        (SELECT seats.id FROM seats WHERE seats.id = 11));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (201, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 67),
        (SELECT seats.id FROM seats WHERE seats.id = 12));

-- ОБРАТНО
-- ПРЯМОЙ РЕЙС 1

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (68, 'OMSVKO', '2023-07-15 01:00:00', '2023-07-15 02:00:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (202, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 68),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (203, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 68),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (204, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 68),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- ПРЯМОЙ РЕЙС 2

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (69, 'OMSVKO', '2023-07-15 12:40:00', '2023-07-15 13:40:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 2), 2, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (205, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 69),
        (SELECT seats.id FROM seats WHERE seats.id = 4));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (206, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 69),
        (SELECT seats.id FROM seats WHERE seats.id = 5));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (207, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 69),
        (SELECT seats.id FROM seats WHERE seats.id = 6));

-- НЕПРЯМОЙ РЕЙС

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (70, 'OMSKZN', '2023-07-15 02:30:00', '2023-07-15 03:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 2, 3);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (208, 500, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 70),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (209, 600, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 70),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (210, 650, true, true, true,
        (SELECT flights.id FROM flights WHERE flights.id = 70),
        (SELECT seats.id FROM seats WHERE seats.id = 9));

INSERT INTO flights (id, code, departure_date, arrival_date, flight_status, aircraft_id, from_id, to_id)
VALUES (71, 'KZNVKO', '2023-07-15 06:45:00', '2023-07-15 08:30:00', 'ON_TIME',
        (SELECT aircrafts.id FROM aircrafts WHERE aircrafts.id = 3), 3, 1);
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (211, 500, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 71),
        (SELECT seats.id FROM seats WHERE seats.id = 7));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (212, 600, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 71),
        (SELECT seats.id FROM seats WHERE seats.id = 8));
INSERT INTO flight_seats (id, fare, is_registered, is_sold, is_booked, flight_id, seat_id)
VALUES (213, 650, false, false, false,
        (SELECT flights.id FROM flights WHERE flights.id = 71),
        (SELECT seats.id FROM seats WHERE seats.id = 9));



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