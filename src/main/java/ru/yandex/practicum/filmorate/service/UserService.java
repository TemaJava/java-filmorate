package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {
    private int generatedId;
    private Map<Integer, User> userMap;

    public UserService() {
        this.generatedId = 1;
        this.userMap = new HashMap<>();
    }

    public User addUser(User user) {
        loginValidation(user);
        user.setId(generatedId);
        userMap.put(generatedId, user);
        generatedId++;
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    public User updateUser(User user) {
        loginValidation(user);
        if (userMap.containsKey(user.getId())) {
            userMap.replace(user.getId(), user);
            log.info("Пользователь успешно обновлен: {}", user);
            return user;
        } else {
            log.error("Пользователь для обновления не найден: {}", user);
            throw new RuntimeException("Пользователь для обновления не найден");
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void loginValidation(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ") || user.getLogin() == null) {
            log.error("Ошибка логина: {}", user.getLogin());
            throw new ValidationException("Ошибка логина. Он не должен быть пустым или включать в себя пробелы");
        }
    }
}
