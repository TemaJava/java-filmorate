package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.interfaces.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY genre_id", this::mapRowToGenre);
    }

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?", this::mapRowToGenre, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Genre> getByFilmId(int filmId) {
        try {
            return jdbcTemplate.query("SELECT fg.genre_id AS genre_id, g.name AS name " +
                    "FROM film_genre AS fg " +
                    "JOIN genres AS g ON fg.genre_id = g.genre_id " +
                    "WHERE fg.film_id = ? ", this::mapRowToGenre, filmId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
