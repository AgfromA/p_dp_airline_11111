DELETE FROM passengers;

ALTER TABLE passengers ADD CONSTRAINT unique_email_constraint UNIQUE (email);

INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Пассажирка', 'Петровна', 'Иванова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'ivanova@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ирина', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova111@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Анна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova222@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Евгения', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova333@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ульяна', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova444@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Ольга', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova555@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Елена', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova666@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');
INSERT INTO passengers(first_name, last_name, middle_name, birth_date, gender, email, phone_number,
                       serial_number_passport, passport_issuing_date, passport_issuing_country)
VALUES ('Людмила', 'Сидоровна', 'Сидорова', TO_DATE('1986/03/30', 'YYYY/MM/DD'), 'FEMALE', 'sidorova777@mail.ru',
        '79333333333', '3333 333333', TO_DATE('2006/03/30', 'YYYY/MM/DD'), 'Россия');