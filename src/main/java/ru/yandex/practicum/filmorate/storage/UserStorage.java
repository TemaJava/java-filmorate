package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);
    User deleteUserById(int id);
    User updateUser(User user);
    Map<Integer, User> getAllUsers();
    User getUserById(int id);
}
