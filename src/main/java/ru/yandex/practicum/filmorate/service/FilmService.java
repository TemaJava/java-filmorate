package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {
    private int generatedId;
    private Map<Integer, Film> filmMap;

    public FilmService() {
        this.generatedId = 0;
        this.filmMap = new HashMap<>();
    }

    public Film addFilm(Film film) {
        releaseDateValidation(film);
        generatedId++;
        film.setId(generatedId);
        filmMap.put(generatedId, film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        releaseDateValidation(film);
        if (filmMap.containsKey(film.getId())) {
            filmMap.replace(film.getId(), film);
            log.info("Фильм успешно обновлен: {}", film);
            return film;
        } else {
            log.error("Ошибка, фильм для обновления не найден: {}", film);
            throw new RuntimeException("Такой фильм для обновления не существует");
        }
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    public void releaseDateValidation(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка даты: {}", film.getReleaseDate());
            throw new ValidationException("Ошибка даты создания");
        }
    }
}
