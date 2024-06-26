CREATE TABLE IF NOT EXISTS users (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE CHECK (birthday < CURDATE() ),
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS mpa (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name ENUM('G', 'PG', 'PG-13', 'R', 'NC-17') NOT NULL
);

CREATE TABLE IF NOT EXISTS film (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200),
    release_date DATE CHECK (release_date >= '1895-12-28' ),
    duration INT CHECK (duration > 0),
    mpa_id INT,
    FOREIGN KEY (mpa_id) REFERENCES mpa(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genre (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name ENUM('Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик') NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE RESTRICT,
    UNIQUE (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_like (
    film_id INT,
    user_id INT,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users_friend (
    user_id INT,
    friend_id INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);