SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE USERS;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE FRIENDSHIP;
ALTER TABLE FRIENDSHIP ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE genres;
ALTER TABLE genres ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE films;
ALTER TABLE films ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE film_genre;
ALTER TABLE film_genre ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE likes;
ALTER TABLE likes ALTER COLUMN ID RESTART WITH 1;
TRUNCATE TABLE ratings;
ALTER TABLE ratings ALTER COLUMN ID RESTART WITH 1;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO ratings (name)
VALUES('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

INSERT INTO genres (title)
VALUES('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

INSERT INTO users (email, login, name, birthday)
VALUES ('aadd@email', 'login', 'name', '1995-08-10');

INSERT INTO users (email, login, name, birthday)
VALUES ('qwe@email', 'qwe', 'qwer', '1995-08-10');

INSERT INTO users (email, login, name, birthday)
VALUES ('zxc@email', 'zxc', 'zxc', '1995-08-10');


INSERT INTO films (name, description, realise_date, duration, rating_id, count_likes)
VALUES ('qwe', 'qwer', '2010-05-06', 100, 1, 0);

INSERT INTO films (name, description, realise_date, duration, rating_id, count_likes)
VALUES ('asd', 'asdf', '2010-05-06', 100, 2, 0);

INSERT INTO films (name, description, realise_date, duration, rating_id, count_likes)
VALUES ('zxc', 'zxcv', '2010-05-06', 100, 3, 0);

INSERT INTO likes (film_id, user_id)
VALUES (1, 1);

UPDATE films SET COUNT_LIKES = 1 WHERE id = 1;