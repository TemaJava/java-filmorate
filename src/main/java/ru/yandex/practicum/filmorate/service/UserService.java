package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage storage;
    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getAllUsers() {
        Map<Integer, User> userMap = storage.getAllUsers();
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(int id) {
        if (storage.getAllUsers().containsKey(id)) {
            return storage.getUserById(id);
        } else {
            log.error("Не найден пользователь с id {}", id);
            throw new NotFoundException("Пользователь с id " + id  + " не найден");
        }
    }
    public List<User> addFriend(int firstId, int secondId) {
        if (!storage.getAllUsers().containsKey(firstId) || !storage.getAllUsers().containsKey(secondId)) {
            log.error("Не найден пользователь с id: {} или {}", firstId, secondId);
            throw new NotFoundException("Не найден пользователь с id" + firstId + " или " + secondId);
        }
        if (storage.getUserById(firstId).getFriends().contains(secondId)) {
            log.error("Пользователи с id: {} и {} уже друзья", firstId, secondId);
            throw new RuntimeException("Пользователи с id" + firstId + " и " + secondId + " уже друзья");
        }
        storage.getUserById(firstId).getFriends().add(secondId);
        storage.getUserById(secondId).getFriends().add(firstId);
        log.info("Пользователи {} и {} добавлены в друзья", storage.getUserById(firstId),
                storage.getUserById(secondId));
        return Arrays.asList(storage.getUserById(firstId), storage.getUserById(secondId));
    }

    public List<User> deleteFriend(int firstId, int secondId) {
        if (!storage.getAllUsers().containsKey(firstId) || !storage.getAllUsers().containsKey(secondId)) {
            log.error("Не найден пользователь с id: {} или {}", firstId, secondId);
            throw new NotFoundException("Не найден пользователь с id" + firstId + " или " + secondId);
        }
        if (!storage.getUserById(firstId).getFriends().contains(secondId)) {
            log.error("Пользователи с id: {} и {} уже не являются друзьями", firstId, secondId);
            throw new RuntimeException("Пользователи с id" + firstId + " и " +
                    secondId + " уже не являются друзьями");
        }
        storage.getUserById(firstId).getFriends().remove(secondId);
        storage.getUserById(secondId).getFriends().remove(firstId);
        log.info("Пользователи {} и {} удалены из друзей",
                storage.getUserById(firstId), storage.getUserById(secondId));
        return Arrays.asList(storage.getUserById(firstId), storage.getUserById(secondId));
    }



    public List<User> getUserFriends(int id) {
        if (storage.getAllUsers().containsKey(id)) {
            List<User> friends = new ArrayList<>();
            for (Integer friend : storage.getUserById(id).getFriends()) {
                friends.add(storage.getUserById(friend));
            }
            return friends;
        } else {
            log.error("Не найден пользователь с id {}", id);
            throw new NotFoundException("Пользователь с id " + id  + " не найден");
        }
    }

    public List<User> getAllCommonFriends(int firstId, int secondId){
        if (!storage.getAllUsers().containsKey(firstId) || !storage.getAllUsers().containsKey(secondId)) {
            log.error("Не найден пользователь с id: {} или {}", firstId, secondId);
            throw new NotFoundException("Не найден пользователь с id" + firstId + " или " + secondId);
        }
        if (storage.getUserById(firstId).getFriends().size()==0 ||
                storage.getUserById(secondId).getFriends().size() == 0) {
                return new ArrayList<>();
        }
        List<Integer> firstFriends = new ArrayList<>(storage.getUserById(firstId).getFriends());
        List<Integer> secondFriends = new ArrayList<>(storage.getUserById(secondId).getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : firstFriends) {
            if (secondFriends.contains(friendId)) {
                commonFriends.add(storage.getUserById(friendId));
            }
        }
        log.info("Список общих друзей: " + commonFriends);
        return commonFriends;
    }
}

