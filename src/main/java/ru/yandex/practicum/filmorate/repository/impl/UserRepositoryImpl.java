package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.interfaces.GenreRepository;
import ru.yandex.practicum.filmorate.repository.interfaces.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepository genreRepository;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query( "SELECT * FROM users", this::mapRowToUser);
    }

    @Override
    public User getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", this::mapRowToUser, id);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("emailname", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());
        int userId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        return getById(userId);
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET EMAILNAME = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getById(user.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    @Override
    public List<User> getFriends(int id) {
        return jdbcTemplate.query("SELECT USERS.USER_ID, EMAILNAME, login, name, birthday  FROM USERS " +
                "LEFT JOIN friendship f on users.USER_ID = f.USER_2_ID where f.USER_1_ID = ?", this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(int user1Id, int user2Id) {
        return jdbcTemplate.query("SELECT u.USER_ID, EMAILNAME, login, name, birthday " +
                        "FROM friendship AS f " +
                        "LEFT JOIN users u ON u.USER_ID = f.USER_2_ID " +
                        "WHERE f.USER_1_ID = ? AND f.USER_2_ID IN ( " +
                        "SELECT f.USER_2_ID " +
                        "FROM friendship AS f " +
                        "LEFT JOIN users AS u ON u.USER_ID = f.USER_2_ID " +
                        "WHERE f.USER_1_ID = ? )",
                this::mapRowToUser, user1Id, user2Id);
    }

    @Override
    public void addFriend(int user1Id, int user2id) {
        jdbcTemplate.update(
                "MERGE INTO FRIENDSHIP (USER_1_ID, USER_2_ID) " +
                        "VALUES (?, ?)",
                user1Id, user2id);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("DELETE " +
                "FROM friendship " +
                "WHERE " +
                "user_1_id = ? AND user_2_id = ? ", userId, friendId);
    }
    
    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("emailname"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
