package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(int id);
    List<Film> getAllFilms();
    Film addLike(int filmId, int userId);
    Film deleteLike(int filmId, int userId);
    List<Film> getMostLikedFilms(int num);
}
