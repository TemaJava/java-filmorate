package ru.yandex.practicum.filmorate.repository.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    User getById(int id);
    User create(User user);
    User update(User user);
    void delete(int id);
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> getFriends(int id);
    List<User> getCommonFriends(int userId, int otherUserId);
}
