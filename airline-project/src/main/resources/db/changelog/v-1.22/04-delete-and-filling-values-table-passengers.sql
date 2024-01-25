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