package ru.yandex.practicum.filmorate.repository.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> getAll();
    Film getById(int id);
    Film create(Film film);
    Film update(Film film);
    void delete(int id);
    List<Film> getPopularFilms(int count);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
}
