package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    List<User> getAll();

    User getById(int id);

    User addUser(UserDto dto);

    User updateUser(UserDto dto);

    User delete(int id);
    void addFriend(int userId1, int userId2);
    void deleteFriend(int userId1, int userId2);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId1, int userId2);
}

