package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.interfaces.FilmRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class FilmRepositoryImpl implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepositoryImpl genreRepository;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        int filmId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        updateFilmGenres(film.getGenres(), filmId);
        return getById(filmId);
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("SELECT * FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id", this::mapRowToFilm);
    }

    @Override
    public Film getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "WHERE f.film_id = ?", this::mapRowToFilm, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query("SELECT *, " +
                "COUNT(l.film_id) AS likes " +
                "FROM films AS f " +
                "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "JOIN film_genre AS fg ON fg.film_id = f.film_id " +
                "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, l.user_id, m.mpa_id, m.name " +
                "ORDER BY likes DESC, f.name LIMIT ?", this::mapRowToFilm, Math.max(count, 0));
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(
                "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                        "duration = ?, mpa_id = ? WHERE film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        updateFilmGenres(film.getGenres(), film.getId());
        return getById(film.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update( "DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO likes VALUES(?, ?)", filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    private void updateFilmGenres(List<Genre> genres, int filmId) {
        if (genres == null) {
            return;
        }
        List<Integer> genreUniqueIds = genres.stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO film_genre VALUES(?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        int genreId = genreUniqueIds.get(i);
                        ps.setInt(1, filmId);
                        ps.setInt(2, genreId);
                    }

                    public int getBatchSize() {
                        return genreUniqueIds.size();
                    }
                });
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa.name")))
                .genres(genreRepository.getByFilmId(resultSet.getInt("film_id")))
                .build();
    }
}
