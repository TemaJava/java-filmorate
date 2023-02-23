package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);
    User update(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    User deleteUserById(int id);
    List<User> addFriend(int userId, int friendId);
    List<User> deleteFriend(int userId, int friendId);
    List<User> getUserFriendsById(int id);
    List<User> getCommonFriends(int userId, int user2Id);

}
