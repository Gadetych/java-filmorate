CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email    VARCHAR(40) UNIQUE,
    login    VARCHAR(40) NOT NULL UNIQUE,
    name     VARCHAR(40),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship
(
    id        integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id   integer REFERENCES USERS (ID),
    friend_id integer REFERENCES USERS (ID),
    status    VARCHAR(9) NOT NULL,
    CHECK (status IN ('PENDING', 'CONFIRMED'))
);

CREATE TABLE IF NOT EXISTS ratings
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(40) NOT NULL,
    description  VARCHAR(200),
    realise_date DATE,
    duration     INTEGER CHECK (duration > 0),
    count_likes  INTEGER CHECK (count_likes > 0),
    rating_id    INTEGER REFERENCES RATINGS (ID)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INTEGER REFERENCES FILMS (ID),
    user_id INTEGER REFERENCES USERS (ID),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    ID    INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TITLE VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER REFERENCES FILMS (ID),
    GENRE_ID INTEGER REFERENCES GENRES (ID),
    PRIMARY KEY (FILM_ID, GENRE_ID)
);