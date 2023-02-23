package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    Film create(Film film);
    Film update(Film film);
    List<Film> getAllFilms();
    Film getById(int id);
    Film deleteFilmById(int id);
    List<Film> getPopularFilms(int num);
    Film addLike(int filmId, int userId);
    Film deleteLike(int filmId, int userId);


}
