package ru.yandex.practicum.filmorate.repository.impl;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Objects;


@Repository
@EnableAutoConfiguration
public class FilmRepositoryImpl implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //используя пример springframework.guru
    @Override
    public Film create(Film film) {
        if (!(film.getReleaseDate()).isAfter(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка даты добавления фильма");
        }

        String queryToCreateFilm = "INSERT INTO films (name, description, release_date, duration) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolderId = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(queryToCreateFilm, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolderId );
        film.setId(Objects.requireNonNull(keyHolderId.getKey()).intValue());

        int filmId = film.getId();
        //создаем табличные записи о связях между id фильма и mpa, genres
        String queryInsertMpa = "INSERT INTO film_mpa (film_id, mpa_id) VALUES (?, ?) ";
        String queryInsertGenres = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?) ";
        String queryValidationGenre = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(queryInsertMpa, filmId, film.getMpa().getId());
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                //Проверка необходима, чтобы жанры добавлялись без дубликатов
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryValidationGenre, filmId, g.getId());
                if (!rowSet.next()) {
                    jdbcTemplate.update(queryInsertGenres, filmId, g.getId());
                }
            }
        }
        //передаем в поля фильма ссылки на его атрибуты
        film.setMpa(getMpaFromBd(filmId));
        film.setGenres(getGenresFromBd(filmId));
        return film;
    }

    @Override
    public Film update(Film film) {
        validateFilm(film.getId());
        if (!(film.getReleaseDate()).isAfter(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка даты добавления фильма");
        }

        String queryUpdateFlm = "UPDATE films SET name = ?, description = ?, " +
                "release_date = ?, duration = ? WHERE id = ?";

        //если у переданного фильма есть жанры - обновляем данные в таблицах
        if (film.getGenres() != null) {
            String queryDeleteGenres = "DELETE FROM film_genre WHERE film_id = ? ";
            String queryUpdateGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?) ";
            String queryValidationGenre = "SELECT * FROM film_genre WHERE film_id = ? AND genre_id = ?";

            jdbcTemplate.update(queryDeleteGenres, film.getId());
            for (Genre g : film.getGenres()) {
                SqlRowSet rowSetGenre = jdbcTemplate.queryForRowSet(queryValidationGenre, film.getId(), g.getId());
                if (!rowSetGenre.next()) {
                    jdbcTemplate.update(queryUpdateGenre, film.getId(), g.getId());
                }
            }
        }
        film.setGenres(getGenresFromBd(film.getId()));
        //если у переданного фильма есть мпа - обновляем данные в таблицах
        if (film.getMpa() != null) {
            String queryDeleteMpa = "DELETE FROM film_mpa WHERE film_id = ?";
            String queryUpdateMpa = "INSERT INTO film_mpa (film_id, mpa_id) VALUES (?, ?)";
            jdbcTemplate.update(queryDeleteMpa, film.getId());
            jdbcTemplate.update(queryUpdateMpa, film.getId(), film.getMpa().getId());
        }
        film.setMpa(getMpaFromBd(film.getId()));
        jdbcTemplate.update(queryUpdateFlm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("SELECT * FROM films", this::createFilmFromRow);
    }

    @Override
    public Film getById(int id) {
        //проверка наличия запрашиваемого фильма
        validateFilm(id);
        return jdbcTemplate.queryForObject("SELECT * FROM films WHERE id = ?", this::createFilmFromRow, id);
    }

    @Override
    public Film deleteFilmById(int id) {
        //проверка наличия запрашиваемого фильма
        validateFilm(id);
        //получаем фильм перед удалением, чтобы его вернуть
        Film film = getById(id);

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", id);
        //очищаем все упоминания фильма из таблиц
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE  FILM_ID = ?", id);
        jdbcTemplate.update("DELETE FROM FILM_MPA WHERE FILM_ID = ?", id);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int num) {
        String query = "SELECT id, f.name, description, release_date, duration FROM FILMS AS f " +
                "LEFT JOIN film_likes AS fl on f.ID = fl.FILM_ID GROUP BY f.id, fl.film_id IN " +
                "(SELECT film_id FROM film_likes) ORDER BY COUNT(fl.film_id) DESC LIMIT ?";
        return jdbcTemplate.query(query, this::createFilmFromRow, num);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);

        jdbcTemplate.update("INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
        return getById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        validateFilm(filmId);
        validateUser(userId);

        jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?", filmId, userId);
        return getById(filmId);
    }



    //реализация своеобразного мапера фильмов
    private Film createFilmFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String desc = resultSet.getString("description");
        LocalDate date = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration");
        return new Film(id, name, desc, date, duration, getMpaFromBd(id), getGenresFromBd(id));

    }

    private Mpa getMpaFromBd(int filmId) {
        String queryToGetMap = "SELECT mpa.mpa_id, name FROM mpa LEFT JOIN film_mpa AS fm " +
                "ON mpa.MPA_ID = fm.mpa_id WHERE film_id = ?";
        return jdbcTemplate.queryForObject(queryToGetMap, this::createMpaFromRow, filmId);
    }

    private Mpa createMpaFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("mpa_id");
        String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

    private List<Genre> getGenresFromBd(int filmId) {
        String query = "SELECT g.genre_id, name FROM genres AS g " +
                "LEFT JOIN film_genre AS fg ON g.genre_id = fg.genre_id WHERE film_id = ?";
        return jdbcTemplate.query(query, this::createGenreFromRow, filmId);
    }

    private Genre createGenreFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    //Проводим проверку, что обновляемый фильм существует в бд
    private void validateFilm(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE ID = ?", id);
        //Первая строка - название колонок. Если нет второй строки - запрос не вернул данные
        if (!rowSet.next()) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private void validateUser(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        if (!rowSet.next()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

}
