UPDATE booking SET passenger_id=1 WHERE passenger_id=4;
UPDATE booking SET passenger_id=2 WHERE passenger_id=5;

UPDATE tickets SET passenger_id=1 where passenger_id=4;
UPDATE tickets SET passenger_id=2 where passenger_id=5;


DELETE FROM passengers where id=4;
DELETE FROM passengers where id=5;

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Светлана', 'Сидорова', 'Сидоровна', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova11@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Мария', 'Уварова', 'Сидоровна', TO_DATE('1990/03/30', 'YYYY/MM/DD'), 'FEMALE', 'uvarova@mail.ru',
        '79333333333', '5555 555555', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');

UPDATE booking SET passenger_id=3 WHERE passenger_id=6;
UPDATE booking SET passenger_id=4 WHERE passenger_id=7;
UPDATE tickets SET passenger_id=3 where passenger_id=6;
UPDATE tickets SET passenger_id=4 where passenger_id=7;
UPDATE tickets SET passenger_id=5 where passenger_id=8;

DELETE FROM passengers where id=6;

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Пассажирка', 'Петровна', 'Иванова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'ivanova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ирина', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Анна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Евгения', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ульяна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ольга', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Елена', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Людмила', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
UPDATE tickets SET passenger_id=14 where passenger_id=13;
UPDATE tickets SET passenger_id=15 where passenger_id=8;