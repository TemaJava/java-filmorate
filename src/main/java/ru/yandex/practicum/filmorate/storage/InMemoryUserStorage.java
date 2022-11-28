package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@Repository
public class InMemoryUserStorage implements UserStorage {
    private int generatedId;
    final private Map<Integer, User> userMap;

    public InMemoryUserStorage() {
        this.generatedId = 0;
        this.userMap = new HashMap<>();
    }

    public User addUser(User user) {
        loginValidation(user);
        generatedId++;
        user.setId(generatedId);
        user.setFriends(new HashSet<>());
        userMap.put(generatedId, user);
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    public User updateUser(User user) {
        loginValidation(user);
        if (userMap.containsKey(user.getId())) {
            user.setFriends(userMap.get(user.getId()).getFriends());
            userMap.replace(user.getId(), user);
            log.info("Пользователь успешно обновлен: {}", user);
            return user;
        } else {
            log.error("Пользователь для обновления не найден: {}", user);
            throw new RuntimeException("Пользователь для обновления не найден");
        }
    }

    public User getUserById(int id) {
        User user;
        if (userMap.containsKey(id)) {
            user = userMap.get(id);
        } else {
            log.error("Пользователь для получения не найден по id: {}", id);
            throw new RuntimeException("Пользователь для получения не найден");
        }
        return user;
    }

    public Map<Integer, User> getAllUsers() {
        return new HashMap<>(userMap);
    }

    public User deleteUserById(int id) {
        User user;
        if (userMap.containsKey(id)) {
            user = userMap.get(id);
            userMap.remove(id);
        } else {
            log.error("Пользователь для удаления не найден по id: {}", id);
            throw new RuntimeException("Пользователь для удаления не найден");
        }
        return user;
    }

    public void loginValidation(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            log.error("Ошибка логина: {}", user.getLogin());
            throw new ValidationException("Ошибка логина. Он не должен быть пустым или включать в себя пробелы");
        }
    }
}
