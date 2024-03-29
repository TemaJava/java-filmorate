package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Repository
public class InMemoryFilmStorage implements FilmStorage{
    private int generatedId;
    final private Map<Integer, Film> filmMap;

    public InMemoryFilmStorage() {
        this.generatedId = 0;
        this.filmMap = new HashMap<>();
    }

    public Film addFilm(Film film) {
        releaseDateValidation(film);
        generatedId++;
        film.setId(generatedId);
        film.setLikes(new HashSet<>());
        filmMap.put(generatedId, film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    public Film getFilmById(int id) {
        Film film;
        if (filmMap.containsKey(id)) {
            film = filmMap.get(id);
        } else {
            log.error("Ошибка. Не найден фильм для получения с id: {}", id);
            throw new RuntimeException("Фильм для получения не найден");
        }
        return film;
    }

    public Film updateFilm(Film film) {
        releaseDateValidation(film);
        if (filmMap.containsKey(film.getId())) {
            film.setLikes(filmMap.get(film.getId()).getLikes());
            filmMap.replace(film.getId(), film);
            log.info("Фильм успешно обновлен: {}", film);
            return film;
        } else {
            log.error("Ошибка, фильм для обновления не найден: {}", film);
            throw new RuntimeException("Такой фильм для обновления не существует");
        }
    }

    public Map<Integer, Film> getAllFilms() {
        return filmMap;
    }

    public void releaseDateValidation(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка даты: {}", film.getReleaseDate());
            throw new ValidationException("Ошибка даты создания");
        }
    }

    public Film deleteFilmById(int id) {
        Film film;
        if (filmMap.containsKey(id)) {
            film = filmMap.get(id);
            filmMap.remove(id);
        } else {
            log.error("Ошибка. Не найден фильм для удаления с id: {}", id);
            throw new RuntimeException("Фильм для удаления не найден");
        }
        return film;
    }
}
