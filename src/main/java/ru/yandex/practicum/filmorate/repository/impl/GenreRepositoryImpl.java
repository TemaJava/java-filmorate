package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) {
        validateGenre(id);
        return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?",
                this::createGenreFromRow, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", this::createGenreFromRow);
    }

    private Genre createGenreFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    private void validateGenre(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", id);
        if (!rowSet.next()) {
            throw new NotFoundException("Жанр не обнаружен");
        }
    }
}
