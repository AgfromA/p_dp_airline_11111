INSERT INTO roles (role_name, role_id)
VALUES ('ROLE_ADMIN', 1);
INSERT INTO roles (role_name, role_id)
VALUES ('ROLE_MANAGER', 2);

INSERT INTO account (first_name, last_name, birth_date, phone_number, email, password, security_question, answer_question, account_id)
VALUES ('Admin4ik2', 'Adminov2', TO_DATE('1999/09/09', 'YYYY/MM/DD'), '79777776777',  'admin3@mail.ru',
        'Admin125@', 'вопрос 11', 'ответ 11', 4);

INSERT INTO account (first_name, last_name, birth_date, phone_number, email, password, security_question, answer_question, account_id)
VALUES ('Манагер5', 'Манагеров5', TO_DATE('2000/02/02', 'YYYY/MM/DD'), '79666656666', 'manager5@mail.ru',
        'Manager153@', 'вопрос 12', 'ответ 12', 5);

INSERT INTO account (first_name, last_name, birth_date, phone_number, email, password, security_question, answer_question, account_id)
VALUES ('Манагер10', 'Манагеров10', TO_DATE('2000/02/02', 'YYYY/MM/DD'), '79666556666', 'manager10@mail.ru',
        'Manager1530@', 'вопрос 10', 'ответ 10', 6);


insert into account_roles (account_id, role_id)
values (4, 1);
insert into account_roles (account_id, role_id)
values (5, 2);
insert into account_roles (account_id, role_id)
values (6, 2);

