package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService service = new FilmService();

    @PostMapping
    public Film createFilm(@Validated @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Validated @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }
}
