package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RawMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.interfaces.UserRepository;


import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final RawMapper<UserDto, User> userDtoToUserMapper;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("пользователь не найден");
        }
        return userRepository.getById(id);
    }

    @Override
    public User addUser(UserDto dto) {
        User newUser = userDtoToUserMapper.mapFrom(dto);
        return userRepository.create(newUser);
    }

    @Override
    public User updateUser(UserDto dto) {
        User user = userDtoToUserMapper.mapFrom(dto);
        if (userRepository.getById(user.getId()) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        } else {
            return userRepository.update(user);
        }
    }

    @Override
    public User delete(int id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        } else {
            User user = userRepository.getById(id);
            userRepository.delete(id);
            return user;
        }
    }

    @Override
    public void addFriend(int userId1, int userId2) {
        if (userId1 == userId2) {
            throw new RuntimeException("Человек не может добавить в друзья сам себя");
        }
        if (userRepository.getById(userId1) == null || userRepository.getById(userId2) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        userRepository.addFriend(userId1, userId2);
    }

    @Override
    public void deleteFriend(int userId1, int userId2) {
            if (userId1 == userId2) {
                throw new RuntimeException("Человек не может удалить из друзей сам себя");
            }
            if (userRepository.getById(userId1) == null || userRepository.getById(userId2) == null) {
                throw new NotFoundException("Такого пользователя не существует");
            }
         userRepository.deleteFriend(userId1, userId2);

    }

    @Override
    public List<User> getFriends(int id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        } else {
            return userRepository.getFriends(id);
        }
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        if (userId1 == userId2) {
            throw new RuntimeException("Ошибка - введен айди одного человека");
        }
        if (userRepository.getById(userId1) == null || userRepository.getById(userId2) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }

        return userRepository.getCommonFriends(userId1, userId2);
    }
}

