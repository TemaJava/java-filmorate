package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return service.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return service.getMostLikedFilms(count);
    }

    @PostMapping
    public Film createFilm(@Validated @RequestBody FilmDto film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Validated @RequestBody FilmDto film) {
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable int id) {
        return service.delete(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return service.deleteLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException exception)  {
        return Map.of("error", "Ошибка с поиском объекта",
                "errorMessage", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(final ValidationException exception)  {
        return Map.of("error", "Ошибка валидации",
                "errorMessage", exception.getMessage());
    }
}
