--РОЛИ И ПОЛИТИКИ
CREATE USER client WITH PASSWORD '0zfHLvLIPqWwmx}~ZJkMdM~%mfwCAM5Z';
CREATE USER administrator with password 'a*t9XqS~P2TUrmEy?@eSO7qoAeZppAgk';

REVOKE ALL PRIVILEGES on SCHEMA public FROM client;
GRANT SELECT on room, room_photo, window_view, service, photo to client;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON ALL TABLES IN SCHEMA public
    TO administrator;

GRANT USAGE, SELECT, UPDATE
    ON ALL SEQUENCES IN SCHEMA public
    TO administrator;

GRANT pg_write_server_files to administrator;

REVOKE DELETE on complaint FROM administrator;

CREATE POLICY smart_delete ON registration
    FOR DELETE
    TO administrator
    USING (
            (SELECT check_out_date
             FROM registration) < (SELECT CURRENT_DATE)
    );


/* Drop Tables */

DROP TABLE IF EXISTS service_registration;
DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS _user;
DROP TABLE IF EXISTS administrator;
DROP TABLE IF EXISTS room_photo;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS complaint;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS photo;
DROP TABLE IF EXISTS service;
DROP TABLE IF EXISTS window_view;




/* Create Tables */

-- Таблица администраторов отеля
CREATE TABLE administrator
(
	-- Уникальный идентификатор администратора
	administrator_id bigserial NOT NULL,
	-- Фамилия администратора
	surname varchar(150) NOT NULL,
	-- Имя администратора
	first_name varchar(150) NOT NULL,
	-- Отчество администратора (необязательно)
	middle_name varchar(150),
	PRIMARY KEY (administrator_id)
) WITHOUT OIDS;


-- Таблица категорий номеров отеля
CREATE TABLE category
(
	-- Уникальный идентификатор категории номеров
	category_id bigserial NOT NULL,
	-- Название категории номеров
	category_name varchar(50) NOT NULL UNIQUE,
	PRIMARY KEY (category_id)
) WITHOUT OIDS;


-- Таблица гостей отеля
CREATE TABLE client
(
	-- Уникальный идентификатор гостя
	client_id bigserial NOT NULL,
	-- Фамилия гостя
	surname varchar(150) NOT NULL,
	-- Имя гостя
	first_name varchar(150) NOT NULL,
	-- Отчество гостя (при наличии)
	middle_name varchar(150),
	-- Наименование документа (ПАСПОРТ/ЗАГРАНПАСПОРТ)
	document_name varchar(14) NOT NULL,
	-- Нномер документа
	document_number varchar(50) NOT NULL,
	PRIMARY KEY (client_id)
) WITHOUT OIDS;


-- Таблица жалоб
CREATE TABLE complaint
(
	-- Уникальный идентификатор жалобы
	complaint_id bigserial NOT NULL,
	-- Уникальный идентификатор гостя
	client_id bigint NOT NULL,
	-- Текст жалобы
	complaint_content varchar(300) NOT NULL UNIQUE,
	PRIMARY KEY (complaint_id)
) WITHOUT OIDS;


-- Таблица оплат
CREATE TABLE payment
(
	-- Уникальный идентификатор оплаты
	payment_id bigserial NOT NULL,
	-- Способ оплаты (НАЛИЧНЫЕ/КАРТА)
	payment_method varchar(8) NOT NULL,
	PRIMARY KEY (payment_id)
) WITHOUT OIDS;


-- Таблица фотографий для номеров
CREATE TABLE photo
(
	-- Уникальный идентификатор фотографии
	-- 
	photo_id bigserial NOT NULL,
	-- URL фотографии
	-- 
	photo_url varchar(300) NOT NULL UNIQUE,
	PRIMARY KEY (photo_id)
) WITHOUT OIDS;


-- Таблица регистраций гостей
CREATE TABLE registration
(
	-- Уникальный идентификатор регистрации
	registration_id bigserial NOT NULL,
	-- Уникальный идентификатор администратора
	administrator_id bigint,
	-- Уникальный идентификатор номера
	room_id bigint NOT NULL,
	-- Уникальный идентификатор оплаты
	payment_id bigint NOT NULL,
	-- Уникальный идентификатор гостя
	client_id bigint NOT NULL,
	-- Дата заселения 
	check_in_date date NOT NULL,
	-- Дата выезда
	check_out_date date NOT NULL,
	-- Итоговая стоимость проживания
	total_price numeric(9,2) NOT NULL,
	-- Поле, отображающее обработона ли заявка
	is_accepted boolean,
	-- Комментарий к заявке
	_comment varchar(300),
	PRIMARY KEY (registration_id)
) WITHOUT OIDS;


-- Таблица номеров отеля
CREATE TABLE room
(
	-- Уникальный идентификатор номера
	room_id bigserial NOT NULL,
	-- Уникальный идентификатор категории номеров
	category_id bigint NOT NULL,
	-- Уникальный идентификатор вида из окна
	window_view_id bigint NOT NULL,
	-- Стоимость номера за одну ночь в рублях
	room_price numeric(9,2) NOT NULL,
	-- Вместимость номера (кол-во человек)
	room_capacity int NOT NULL,
	-- Этаж, на котором располагается номер
	room_floor int NOT NULL,
	-- Наличие кондиционера (есть/нет)
	conditioner_availability boolean NOT NULL,
	-- Наличие фена (есть/нет)
	hair_dryer_availability boolean NOT NULL,
	-- Поле "описание комнаты"
	description text NOT NULL UNIQUE,
	PRIMARY KEY (room_id)
) WITHOUT OIDS;


CREATE TABLE room_photo
(
	-- Уникальный идентификатор фотографии
	-- 
	photo_id bigint NOT NULL,
	-- Уникальный идентификатор номера
	room_id bigint NOT NULL
) WITHOUT OIDS;


-- Таблица услуг, предоставляемых отелем
CREATE TABLE service
(
	-- Уникальный идентификатор услуги
	service_id bigserial NOT NULL,
	-- Название услуги
	service_name varchar(50) NOT NULL UNIQUE,
	-- Стоимость услуги
	service_price numeric(9,2) NOT NULL,
	PRIMARY KEY (service_id)
) WITHOUT OIDS;


CREATE TABLE service_registration
(
	-- Уникальный идентификатор услуги
	service_id bigint NOT NULL,
	-- Уникальный идентификатор регистрации
	registration_id bigint NOT NULL
) WITHOUT OIDS;


-- Таблица наименований видов из окон номеров
CREATE TABLE window_view
(
	-- Уникальный идентификатор вида из окна
	window_view_id bigserial NOT NULL,
	-- Название вида из окна
	window_view_name varchar(30) NOT NULL UNIQUE,
	PRIMARY KEY (window_view_id)
) WITHOUT OIDS;


CREATE TABLE _user
(
	-- Никнейм пользователя
	username varchar(30) NOT NULL UNIQUE,
	-- Пароль пользователя (хранится в зашифрованном виде)
	password varchar(100) NOT NULL,
	-- Роль пользователя (USER/ADMIN)
	role varchar(30) NOT NULL,
	-- Дата регистрации пользователя
	registration_date date NOT NULL,
	-- Уникальный идентификатор гостя
	client_id bigint,
	-- Уникальный идентификатор администратора
	administrator_id bigint,
	PRIMARY KEY (username)
) WITHOUT OIDS;



/* Create Foreign Keys */

ALTER TABLE registration
	ADD FOREIGN KEY (administrator_id)
	REFERENCES administrator (administrator_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE _user
	ADD FOREIGN KEY (administrator_id)
	REFERENCES administrator (administrator_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room
	ADD FOREIGN KEY (category_id)
	REFERENCES category (category_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE complaint
	ADD FOREIGN KEY (client_id)
	REFERENCES client (client_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE registration
	ADD FOREIGN KEY (client_id)
	REFERENCES client (client_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE _user
	ADD FOREIGN KEY (client_id)
	REFERENCES client (client_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE registration
	ADD FOREIGN KEY (payment_id)
	REFERENCES payment (payment_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_photo
	ADD FOREIGN KEY (photo_id)
	REFERENCES photo (photo_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE service_registration
	ADD FOREIGN KEY (registration_id)
	REFERENCES registration (registration_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE registration
	ADD FOREIGN KEY (room_id)
	REFERENCES room (room_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room_photo
	ADD FOREIGN KEY (room_id)
	REFERENCES room (room_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE service_registration
	ADD FOREIGN KEY (service_id)
	REFERENCES service (service_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE room
	ADD FOREIGN KEY (window_view_id)
	REFERENCES window_view (window_view_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Comments */

COMMENT ON TABLE administrator IS 'Таблица администраторов отеля';
COMMENT ON COLUMN administrator.administrator_id IS 'Уникальный идентификатор администратора';
COMMENT ON COLUMN administrator.surname IS 'Фамилия администратора';
COMMENT ON COLUMN administrator.first_name IS 'Имя администратора';
COMMENT ON COLUMN administrator.middle_name IS 'Отчество администратора (необязательно)';
COMMENT ON TABLE category IS 'Таблица категорий номеров отеля';
COMMENT ON COLUMN category.category_id IS 'Уникальный идентификатор категории номеров';
COMMENT ON COLUMN category.category_name IS 'Название категории номеров';
COMMENT ON TABLE client IS 'Таблица гостей отеля';
COMMENT ON COLUMN client.client_id IS 'Уникальный идентификатор гостя';
COMMENT ON COLUMN client.surname IS 'Фамилия гостя';
COMMENT ON COLUMN client.first_name IS 'Имя гостя';
COMMENT ON COLUMN client.middle_name IS 'Отчество гостя (при наличии)';
COMMENT ON COLUMN client.document_name IS 'Наименование документа (ПАСПОРТ/ЗАГРАНПАСПОРТ)';
COMMENT ON COLUMN client.document_number IS 'Нномер документа';
COMMENT ON TABLE complaint IS 'Таблица жалоб';
COMMENT ON COLUMN complaint.complaint_id IS 'Уникальный идентификатор жалобы';
COMMENT ON COLUMN complaint.client_id IS 'Уникальный идентификатор гостя';
COMMENT ON COLUMN complaint.complaint_content IS 'Текст жалобы';
COMMENT ON TABLE payment IS 'Таблица оплат';
COMMENT ON COLUMN payment.payment_id IS 'Уникальный идентификатор оплаты';
COMMENT ON COLUMN payment.payment_method IS 'Способ оплаты (НАЛИЧНЫЕ/КАРТА)';
COMMENT ON TABLE photo IS 'Таблица фотографий для номеров';
COMMENT ON COLUMN photo.photo_id IS 'Уникальный идентификатор фотографии
';
COMMENT ON COLUMN photo.photo_url IS 'URL фотографии
';
COMMENT ON TABLE registration IS 'Таблица регистраций гостей';
COMMENT ON COLUMN registration.registration_id IS 'Уникальный идентификатор регистрации';
COMMENT ON COLUMN registration.administrator_id IS 'Уникальный идентификатор администратора';
COMMENT ON COLUMN registration.room_id IS 'Уникальный идентификатор номера';
COMMENT ON COLUMN registration.payment_id IS 'Уникальный идентификатор оплаты';
COMMENT ON COLUMN registration.client_id IS 'Уникальный идентификатор гостя';
COMMENT ON COLUMN registration.check_in_date IS 'Дата заселения ';
COMMENT ON COLUMN registration.check_out_date IS 'Дата выезда';
COMMENT ON COLUMN registration.total_price IS 'Итоговая стоимость проживания';
COMMENT ON COLUMN registration.is_accepted IS 'Поле, отображающее обработона ли заявка';
COMMENT ON COLUMN registration._comment IS 'Комментарий к заявке';
COMMENT ON TABLE room IS 'Таблица номеров отеля';
COMMENT ON COLUMN room.room_id IS 'Уникальный идентификатор номера';
COMMENT ON COLUMN room.category_id IS 'Уникальный идентификатор категории номеров';
COMMENT ON COLUMN room.window_view_id IS 'Уникальный идентификатор вида из окна';
COMMENT ON COLUMN room.room_price IS 'Стоимость номера за одну ночь в рублях';
COMMENT ON COLUMN room.room_capacity IS 'Вместимость номера (кол-во человек)';
COMMENT ON COLUMN room.room_floor IS 'Этаж, на котором располагается номер';
COMMENT ON COLUMN room.conditioner_availability IS 'Наличие кондиционера (есть/нет)';
COMMENT ON COLUMN room.hair_dryer_availability IS 'Наличие фена (есть/нет)';
COMMENT ON COLUMN room.description IS 'Поле "описание комнаты"';
COMMENT ON COLUMN room_photo.photo_id IS 'Уникальный идентификатор фотографии
';
COMMENT ON COLUMN room_photo.room_id IS 'Уникальный идентификатор номера';
COMMENT ON TABLE service IS 'Таблица услуг, предоставляемых отелем';
COMMENT ON COLUMN service.service_id IS 'Уникальный идентификатор услуги';
COMMENT ON COLUMN service.service_name IS 'Название услуги';
COMMENT ON COLUMN service.service_price IS 'Стоимость услуги';
COMMENT ON COLUMN service_registration.service_id IS 'Уникальный идентификатор услуги';
COMMENT ON COLUMN service_registration.registration_id IS 'Уникальный идентификатор регистрации';
COMMENT ON TABLE window_view IS 'Таблица наименований видов из окон номеров';
COMMENT ON COLUMN window_view.window_view_id IS 'Уникальный идентификатор вида из окна';
COMMENT ON COLUMN window_view.window_view_name IS 'Название вида из окна';
COMMENT ON COLUMN _user.username IS 'Никнейм пользователя';
COMMENT ON COLUMN _user.password IS 'Пароль пользователя (хранится в зашифрованном виде)';
COMMENT ON COLUMN _user.role IS 'Роль пользователя (USER/ADMIN)';
COMMENT ON COLUMN _user.registration_date IS 'Дата регистрации пользователя';
COMMENT ON COLUMN _user.client_id IS 'Уникальный идентификатор гостя';
COMMENT ON COLUMN _user.administrator_id IS 'Уникальный идентификатор администратора';

--ЗАПОЛНЕНИЕ ТАБЛИЦ
INSERT INTO photo(photo_url)
values ('https://ex-terior.ru/wp-content/uploads/2018/05/otel-ritc-karlton2-1024x623.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/otel-balchug-kempinski-moskva1-e1525789527246.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/gostinica-nacional1-1024x681.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/crowne-plaza-moscow-world-trade-centre4-e1535452613493.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/palmira-biznes-klub2-e1547325068855.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/crowne-plaza-moscow-world-trade-centre6-e1535452604929.jpg'),
       ('https://ex-terior.ru/wp-content/uploads/2018/05/otel-katerina-siti1-e1526037932223.jpg'),
       ('https://www.pogostite.ru/images/1280/960/0/admin/images/82/redisson_slavjanskaja_otel_radisson_slavyanskaya_hotel_13.jpg');

insert into category(category_name)
values ('Люкс'),
       ('Полулюкс'),
       ('Комфорт');

insert into window_view (window_view_name)
values ('Кремль'),
       ('река Москва');

insert into room(category_id, window_view_id, room_price, room_capacity, room_floor, conditioner_availability,
                 hair_dryer_availability, description)
values (1, 1, 10000.00, 3, 15, true, true, 'Наш лучший номер с видом на Кремль'),
       (2, 2, 7500.00, 2, 9, false, true, 'Замечательный номер с видом на реку Москва'),
       (3, 2, 5000.00, 1, 4, false, true, 'Отличный номер с видом на реку Москва');

insert into payment (payment_method)
values ('НАЛИЧНЫЕ'),
       ('КАРТА');

insert into service (service_name, service_price)
values ('Завтрак', 1000.00),
       ('Ужин', 1500.00),
       ('Уборка номера', 700.00);

insert into room_photo (photo_id, room_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 3),
       (8, 3);

insert into administrator (surname, first_name, middle_name)
values ('Петров', 'Петр', 'Петрович'),
       ('Иванов', 'Иван', 'Иванович');

insert into _user (username, password, role, registration_date, client_id, administrator_id)
values ('koval', 'qwertyQWERTY12345$', 'ROLE_SUPER_ADMIN', '2022-12-12', null, null),
       ('petrov', 'qwertyQWERTY12345$', 'ROLE_ADMINISTRATOR', '2022-12-12', null, 1),
       ('ivanov', 'qwertyQWERTY12345$', 'ROLE_ADMINISTRATOR', '2022-12-12', null, 2);

--ФУНКЦИЯ ХЭШИРОВАНИЯ ПАРОЛЯ
CREATE FUNCTION hash_password() RETURNS trigger AS
$hash_pass$ -- триггер
BEGIN
    CREATE EXTENSION IF NOT EXISTS pgcrypto;
    update _user set password = crypt(new.password, gen_salt('bf', 8)) WHERE (username = new.username);
    RETURN new;
END
$hash_pass$ LANGUAGE plpgsql;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

--ТРИГГЕР НА ВСТАВКУ В _user
CREATE TRIGGER hash_pass
    after INSERT
    on _user
    for each ROW
EXECUTE PROCEDURE hash_password();

--ПРОЦЕДУРА НА УВЕЛИЧЕНИЕ ТОТАЛЬНОЙ СТОИМОСТИ
CREATE PROCEDURE bigger_price(reg_id bigint, damage_sum INTEGER)
    LANGUAGE SQL
AS
$$
update registration
set total_price = total_price + damage_sum::numeric(9, 2)
where registration_id = reg_id
$$;

--ПРЕДСТАВЛЕНИЕ "Топ 1 сервис"
CREATE view top_service AS
SELECT service_id, count(registration_id)
FROM service_registration
GROUP BY service_id
ORDER BY count(registration_id) DESC
limit 1;

--ФУНКЦИЯ ДОБАВЛЕНИЯ 500 РАНДОМНЫХ ФОТО
CREATE OR REPLACE FUNCTION make_random_photos()
    RETURNS int AS
$$
DECLARE
    url          VARCHAR;
    photos_count integer;
BEGIN
    photos_count := 0;
    url := 'https://photo';

    FOR i IN 1..500
        LOOP
            INSERT INTO photo (photo_url)
            VALUES (url || photos_count || '.ru');
            photos_count := photos_count + 1;
        END LOOP;
    RETURN photos_count;
END;
$$ LANGUAGE plpgsql;

SELECT make_random_photos();

--ИНДЕКС НА photo.photo_id
create index photo_f on photo (photo_id);

--ПРОЦЕДУРА С ТРАНЗАКЦИЕЙ НА БЕСПЛАТНОЕ ДОБАВЛЕНИЕ ВСЕХ ДОП. УСЛУГ, ЕСЛИ НОМЕР ЛЮКС
CREATE PROCEDURE add_services_to_lux_transaction(reg_id bigint)
    LANGUAGE plpgsql
AS
$$
declare
    c       record;
    counter integer;
BEGIN
    counter := 0;
    SAVEPOINT my_savepoint;
    FOR c IN SELECT service_id FROM service
        LOOP
            INSERT INTO service_registration(service_id, registration_id) VALUES (c.service_id, reg_id);
            counter := counter + 1;
        END LOOP;
    if counter = (select count(service_name) from service) then
        COMMIT;
    else
        rollback to savepoint my_savepoint;
    end if;
END;
$$;

--ФУНКЦИЯ СОХРАНЕНИЯ СТАТИСТИКИ В CSV
CREATE FUNCTION save_admin_statistic_to_csv(id bigint,
                                            start_period date,
                                            end_period date,
                                            filepath text) RETURNS void
AS
$$
DECLARE
    all_checkins_count INTEGER; -- Общее кол-во проведённых заселений
    lux_count          INTEGER; -- Кол-во номеров категории люкс, которые продал сотрудник
    total_revenue      NUMERIC(9, 2); -- Тотальная выручка
    statement          TEXT;
BEGIN
    all_checkins_count := (SELECT COUNT(*)
                           FROM registration
                                    inner join administrator
                                               on registration.administrator_id = administrator.administrator_id
                           where ((administrator.administrator_id = id) and
                                  (registration.check_in_date > start_period) and
                                  (registration.check_in_date < end_period)));
    lux_count := (SELECT COUNT(*)
                  FROM registration
                           JOIN room on registration.room_id = room.room_id
                           join administrator on registration.administrator_id = administrator.administrator_id
                  WHERE (room.category_id = 1)
                    and (registration.check_in_date > start_period)
                    and (registration.check_in_date < end_period));
    total_revenue := (SELECT SUM(total_price)
                      FROM registration
                               inner join administrator
                                          on registration.administrator_id = administrator.administrator_id
                      where ((administrator.administrator_id = id) and
                             (registration.check_in_date > start_period) and
                             (registration.check_in_date < end_period)));
    statement := format('copy (SELECT %s AS all_checkins_count, %s AS lux_count, %s AS total_revenue)
to ''%s/admin_statistics.csv'' with csv
header;
',
                        all_checkins_count, lux_count, total_revenue, filepath);
    EXECUTE statement;
END;
$$
    LANGUAGE plpgsql;
