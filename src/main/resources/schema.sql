CREATE TABLE IF NOT EXISTS users (
                                     user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                     emailName VARCHAR(50) UNIQUE NOT NULL,
                                     login VARCHAR(50) NOT NULL,
                                     name VARCHAR(50) NOT NULL,
                                     birthday DATE NOT NULL);

CREATE TABLE IF NOT EXISTS mpa (
                                   mpa_id INTEGER AUTO_INCREMENT PRIMARY KEY ,
                                   name VARCHAR(10) UNIQUE NOT NULL);

CREATE TABLE IF NOT EXISTS genres (
                                      genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
                                     film_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL,
                                     description VARCHAR(200) NOT NULL,
                                     release_date DATE NOT NULL,
                                     duration INTEGER NOT NULL,
                                     mpa_id INTEGER NOT NULL,
                                     FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE RESTRICT);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id INTEGER NOT NULL,
                                          genre_id INTEGER NOT NULL,
                                          FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
                                          FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS likes (
                                     film_id INTEGER NOT NULL,
                                     user_id INTEGER NOT NULL,
                                     FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS friendship (
                                          user_1_id   INTEGER NOT NULL,
                                          user_2_id INTEGER NOT NULL,
                                          FOREIGN KEY (user_1_id) REFERENCES users (user_id) ON DELETE CASCADE,
                                          FOREIGN KEY (user_2_id) REFERENCES users (user_id) ON DELETE CASCADE);

