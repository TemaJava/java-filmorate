package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreServiceInterface {
    Genre getGenreById(int id);
    List<Genre> getAllGenres();
}
