package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {

    List<Film> getAllFilms();

    Film getFilmById(int id);
    Film addFilm(FilmDto dto);
    Film updateFilm(FilmDto dto);
    Film delete(int id);
    Film addLike(int filmId, int userId);
    Film deleteLike(int filmId, int userId);
    List<Film> getMostLikedFilms(int count);

}
