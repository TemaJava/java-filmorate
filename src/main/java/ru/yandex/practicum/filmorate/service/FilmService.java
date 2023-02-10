package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RawMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.interfaces.FilmRepository;
import ru.yandex.practicum.filmorate.repository.interfaces.GenreRepository;
import ru.yandex.practicum.filmorate.repository.interfaces.MpaRepository;
import ru.yandex.practicum.filmorate.repository.interfaces.UserRepository;


import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements FilmServiceInterface {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final RawMapper<FilmDto, Film> filmDtoToFilmMapper;


    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAll();
    }

    @Override
    public Film getFilmById(int id) {
        if (filmRepository.getById(id) == null) {
            throw new NotFoundException("фильм не найден");
        } else {
        return filmRepository.getById(id);
        }
    }

    @Override
    public Film addFilm(FilmDto dto) {
        Film film = filmDtoToFilmMapper.mapFrom(dto);
        int mpa = film.getMpa().getId();
        if (mpaRepository.getById(mpa) == null) {
            throw new NotFoundException("не обнаружен mpa");
        }
        List<Genre> list = film.getGenres();
        for (Genre genre:list) {
            if (genreRepository.getById(genre.getId()) == null) throw new NotFoundException("не обнаружен жанр");
        }

        return filmRepository.create(film);
    }

    @Override
    public Film updateFilm(FilmDto dto) {
        Film filmTest = filmRepository.getById(dto.getId());
        if (filmTest == null) {
            throw new NotFoundException("Не найден фильм");
        } else {
            Film film = filmDtoToFilmMapper.mapFrom(dto);
            int mpa = film.getMpa().getId();
            if (mpaRepository.getById(mpa) == null) {
                throw new NotFoundException("не обнаружен mpa");
            }
            List<Genre> list = film.getGenres();
            for (Genre genre:list) {
                if (genreRepository.getById(genre.getId()) == null) throw new NotFoundException("не обнаружен жанр");
            }
            return filmRepository.update(film);
        }
    }

    @Override
    public Film delete(int id) {
        Film film = filmRepository.getById(id);
        if (film == null) {throw new NotFoundException("Не найден фильм");}
        else {
            filmRepository.delete(id);
            return film;
        }
    }

    @Override
    public Film addLike(int filmId, int userId) {
        Film film = filmRepository.getById(filmId);
        if (film == null) {throw new NotFoundException("Не найден фильм"); }
        else {
            User user = userRepository.getById(userId);
            if (user == null) {
                throw new NotFoundException("Не найден пользователь");
            } else {
                filmRepository.likeFilm(filmId, userId);
            }
        }
        return filmRepository.getById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        Film film = filmRepository.getById(filmId);
        if (film == null) {throw new NotFoundException("Не найден фильм"); }
        else {
            User user = userRepository.getById(userId);
            if (user == null) {
                throw new NotFoundException("Не найден пользователь");
            } else {
                filmRepository.deleteLikeFromFilm(filmId, userId);
            }
        }
        return filmRepository.getById(filmId);
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        return filmRepository.getPopularFilms(count);
    }
}
