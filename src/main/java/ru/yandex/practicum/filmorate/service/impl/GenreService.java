package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreServiceInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService implements GenreServiceInterface {
    private final GenreRepository genreRepository;

    public Genre getGenreById(int id) {
        return genreRepository.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }
}
