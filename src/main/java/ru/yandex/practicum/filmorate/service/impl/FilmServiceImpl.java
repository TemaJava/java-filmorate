package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepositoryImpl filmRepository;

    @Override
    public Film addFilm(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmRepository.update(film);
    }

    @Override
    public Film getFilmById(int id) {
        return filmRepository.getById(id);
    }

    @Override
    public List<Film> getAllFilms() {return filmRepository.getAllFilms();}

    @Override
    public Film deleteFilmById(int id) {
        return filmRepository.deleteFilmById(id);
    }

    @Override
    public List<Film> getPopularFilms(int num) {
        return filmRepository.getPopularFilms(num);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return filmRepository.addLike(filmId, userId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        return filmRepository.deleteLike(filmId, userId);
    }
}
