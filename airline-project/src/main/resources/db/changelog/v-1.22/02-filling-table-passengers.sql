DELETE FROM passengers where id=3;
INSERT INTO passengers (first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                        serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Bob', 'Smith', 'J', TO_DATE('2003/11/09', 'YYYY/MM/DD'), 'MALE', 'smith@mail.ru', '79111111111',
        '0000 000000', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия');

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Игорь', 'Шнуров', 'Петрович', TO_DATE('1986/01/11', 'YYYY/MM/DD'), 'MALE', 'shnur@mail.ru', '79111111111',
        '1111 111111', TO_DATE('2006/01/11', 'YYYY/MM/DD'), 'Россия');

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Демид', 'Игнатьев', 'Иванович', TO_DATE('1986/02/22', 'YYYY/MM/DD'), 'MALE', 'ignatev@mail.ru', '79222222222',
        '2222 222222', TO_DATE('2006/02/22', 'YYYY/MM/DD'), 'Россия');

-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Пассажирка', 'Петровна', 'Иванова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'ivanova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Ирина', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Анна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Евгения', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Ульяна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Ольга', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Елена', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
-- INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
--                        serial_number_passport, passport_issuing_date, passport_issuing_country)
-- VALUES ('Людмила', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova@mail.ru',
--         '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');