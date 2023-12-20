INSERT INTO account (first_name, last_name, birth_date, phone_number, email, password, security_question, answer_question)
VALUES ('Admin4ik', 'Adminov', TO_DATE('1999/09/09', 'YYYY/MM/DD'), '79777777777',  'admin@mail.ru',
        '$2a$10$gH.ED3q0X4RZz2Pcm9/IU.Cz88JM7t6A6Nw9Y5.e8gRcl8GZHz20y', 'вопрос 1', 'ответ 1');

INSERT INTO account (first_name, last_name, birth_date, phone_number, email, password, security_question, answer_question)
VALUES ('Иван', 'Манагеров', TO_DATE('2000/02/02', 'YYYY/MM/DD'), '79666666666', 'manager@mail.ru',
        '$2a$10$tvW02anRGLpXpLYZTx8UAeaUh3KwVukLsPWIckGhdcL41TwdYss1W', 'вопрос 2', 'ответ 2');


INSERT INTO account_roles(account_id, role_id)
VALUES ((SELECT account.account_id FROM account WHERE account.email = 'admin@mail.ru'),
        (SELECT roles.role_id FROM roles WHERE roles.role_name = 'ROLE_ADMIN'));

INSERT INTO account_roles(account_id, role_id)
VALUES ((SELECT account.account_id FROM account WHERE account.email = 'manager@mail.ru'),
        (SELECT roles.role_id FROM roles WHERE roles.role_name = 'ROLE_MANAGER'));