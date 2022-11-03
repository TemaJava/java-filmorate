package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {SpringApplication.run(FilmorateApplication.class, args);}

}
