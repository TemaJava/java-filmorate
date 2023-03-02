package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);
    Film update(Film film);
    Film getFilmById(int id);
    List<Film> getAllFilms();
    Film deleteFilmById(int id);
    Film addLike(int filmId, int userId);
    Film deleteLike(int filmId, int userId);
    List<Film> getPopularFilms(int num);
}
