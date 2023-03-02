package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User updateUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    List<User> addFriend(int firstId, int secondId);
    List<User> deleteFriend(int firstId, int secondId);
    List<User> getUserFriends(int id);
    List<User> getAllCommonFriends(int firstId, int secondId);
}
