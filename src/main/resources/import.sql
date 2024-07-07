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
