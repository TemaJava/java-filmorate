package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);
    Film deleteFilmById(int id);
    Film updateFilm(Film film);
    Map<Integer, Film> getAllFilms();
    Film getFilmById(int id);
}
