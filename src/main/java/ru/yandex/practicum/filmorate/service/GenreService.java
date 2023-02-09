package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.interfaces.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService implements GenreServiceInterface {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Override
    public Genre getById(int id) {
        if (genreRepository.getById(id) == null) {
            throw new NotFoundException("Не найден жанр");
        }
        return genreRepository.getById(id);
    }
}