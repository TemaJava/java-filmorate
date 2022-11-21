package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage storage;

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        Map<Integer, Film> filmMap = storage.getAllFilms();
        return new ArrayList<>(filmMap.values());
    }

    public Film getFilmById(int id) {
        if (storage.getAllFilms().containsKey(id)) {
            return storage.getFilmById(id);
        } else {
            log.error("Фильм с id {} не найден", id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    public Film addLike(int filmId, int userId) {
        if (!storage.getAllFilms().containsKey(filmId)) {
            log.error("Фильм с id {} не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (storage.getFilmById(filmId).getLikes().contains(userId)) {
            log.error("Пользователь {} уже поставил лайк фильму {}", userId, filmId);
            throw new RuntimeException("Пользователь " + userId + " уже ставил лайк фильму " + filmId);
        }
        storage.getFilmById(filmId).getLikes().add(userId);
        log.info("Лайк от пользователя {} для фильма {}успешно добавлен", userId, filmId);
        return storage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        if (!storage.getAllFilms().containsKey(filmId)) {
            log.error("Фильм с id {} не найден", filmId);
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!storage.getFilmById(filmId).getLikes().contains(userId)) {
            log.error("Фильм {} уже не имеет лайка от пользователя {}", filmId, userId);
            throw new RuntimeException("Фильм " + filmId + " уже не имеет лайка от пользователя " + userId);
        }
        storage.getFilmById(filmId).getLikes().remove(userId);
        return storage.getFilmById(filmId);
    }

    public List<Film> getMostLikedFilms(int num) {
        List<Film> likedFilms = new ArrayList<>(storage.getAllFilms().values());
        likedFilms.sort(Film.COMPARE_BY_COUNT);
        List<Film> finalList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            finalList.add(likedFilms.get(i));
        }
        return finalList;
    }
}
