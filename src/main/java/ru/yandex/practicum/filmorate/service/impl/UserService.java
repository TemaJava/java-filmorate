package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;


import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepositoryImpl userRepository;

    public User addUser(User user) {
        return userRepository.create(user);
    }

    public User updateUser(User user) {
        return userRepository.update(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User deleteUserById(int id) {
        return userRepository.deleteUserById(id);
    }

    public List<User> addFriend(int firstId, int secondId) {
        return userRepository.addFriend(firstId, secondId);
    }

    public List<User> deleteFriend(int firstId, int secondId) {
        return userRepository.deleteFriend(firstId, secondId);
    }

    public List<User> getUserFriends(int id) {
        return userRepository.getUserFriendsById(id);
    }

    public List<User> getAllCommonFriends(int firstId, int secondId) {
        return userRepository.getCommonFriends(firstId, secondId);

    }
}

