package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaRepositoryImpl implements MpaRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        validateMpa(id);
        return jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE mpa_id = ?", this::createMpaFromRow, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", this::createMpaFromRow);
    }

    private Mpa createMpaFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("mpa_id");
        String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

    private void validateMpa(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?", id);
        if (!rowSet.next()) {
            throw new NotFoundException("Мпа не найден");
        }
    }
}
