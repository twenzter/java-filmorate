CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    age_rating varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS films  (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    mpa_id integer NOT NULL REFERENCES mpa(id),
    CONSTRAINT date_check CHECK(release_date >= '1895-12-28'),
    CONSTRAINT duration_positive CHECK(duration > 0)
);
CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS films_genres (
    film_id INTEGER REFERENCES films(id),
    genre_id INTEGER REFERENCES genres(id),
    PRIMARY KEY (film_id, genre_id)
);
CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar NOT NULL,
    login varchar NOT NULL,
    name varchar,
    birthday date NOT NULL,
    CONSTRAINT birthday_in_past CHECK(birthday <= CURRENT_DATE)
);
CREATE TABLE IF NOT EXISTS films_likes (
    film_id INTEGER REFERENCES films(id),
    user_id INTEGER REFERENCES users(id),
    PRIMARY KEY(film_id, user_id)
);
CREATE TABLE IF NOT EXISTS friendship_status (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS users_friends (
    user_id INTEGER REFERENCES users(id),
    friend_id INTEGER REFERENCES users(id),
    friendship_status_id INTEGER NOT NULL REFERENCES friendship_status(id),
    PRIMARY KEY(user_id, friend_id)
);