package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String queryCreateUser = "INSERT INTO users (email, LOGIN, NAME, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            final PreparedStatement stmt = con.prepareStatement(queryCreateUser, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        validationUser(user.getId());
        String queryUpdate = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(queryUpdate, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        validationUser(id);
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", this::createUserFromRow, id);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", this::createUserFromRow);
    }

    @Override
    public User deleteUserById(int id) {
        //валидация проводится одновременно с получением пользователя
        User user = getUserById(id);
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
        return user;
    }

    //методы связанные с дружбой
    @Override
    public List<User> addFriend(int userId, int friendId) {
        validationUser(userId);
        validationUser(friendId);

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDSHIP WHERE user_id = ? " +
                "AND friend_id = ?", friendId, userId);
        //.first - выбирает первую строку и возвращает false если её нет
        if (rowSet.first()) {
            jdbcTemplate.update("UPDATE friendship SET user_id = ? " +
                    "WHERE friend_id = ? AND confirmed = ?", userId, friendId, true);
        } else {
            jdbcTemplate.update("INSERT INTO friendship (user_id, friend_id, confirmed) VALUES " +
                    "(?, ?, ?)", userId, friendId, false);
        }
        return List.of(getUserById(userId), getUserById(friendId));
    }

    @Override
    public List<User> deleteFriend(int userId, int friendId) {
        validationUser(userId);
        validationUser(friendId);
        jdbcTemplate.update("DELETE FROM friendship WHERE user_id = ? AND friend_id = ?", userId, friendId);
        return List.of(getUserById(userId), getUserById(friendId));
    }

    @Override
    public List<User> getUserFriendsById(int id) {
        validationUser(id);
        return jdbcTemplate.query("SELECT id, email, login, name, birthday FROM users AS u " +
                "LEFT JOIN friendship as f ON u.id = f.friend_id " +
                "WHERE user_id = ? AND confirmed = true", this::createUserFromRow, id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int user2Id) {
        validationUser(userId);
        validationUser(user2Id);
        String query = "SELECT id, email, login, name, birthday FROM friendship AS fs " +
                "LEFT JOIN users AS u ON u.id = fs.friend_id " +
                "WHERE fs.user_id AND fs.friend_id IN (SELECT friend_id " +
                "FROM friendship AS fs LEFT JOIN users AS u ON u.id = fs.friend_id " +
                "WHERE fs.user_id = ?)";
        return jdbcTemplate.query(query, this::createUserFromRow, userId, user2Id);
    }
    private User createUserFromRow(ResultSet resultSet, int row) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, name, email, login, birthday);
    }

    //валидация по аналогии с репозиторием фильмов
    private void validationUser(int id) {
        String userValidation = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(userValidation, id);
        if (!userRows.next()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }


}
