package ru.yandex.practicum.filmorate.daoTests.film;


import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql (statements = "DELETE FROM FILM_MPA")
@Sql (statements = "DELETE FROM FILM_LIKES")
@Sql (statements = "DELETE FROM FILM_GENRE")
@Sql (statements = "DELETE FROM FILMS")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTest {
    private final FilmRepositoryImpl filmRepository;

    private final UserRepositoryImpl userRepository;

    @Test
    void getEmptyFilmsList() {
        Assertions.assertEquals(Collections.emptyList(), filmRepository.getAllFilms());
    }

    @Test
    void createFilmTest() {
         Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
         filmRepository.create(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        Assertions.assertEquals("testNameFilm", filmRepository.getById(film.getId()).getName());
        Assertions.assertEquals("testDesc", filmRepository.getById(film.getId()).getDescription());
    }

    @Test
    void getUnavailableFilmThrowsExceptionTest() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        AssertionsForClassTypes.assertThatThrownBy(() -> filmRepository.getById(film.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
     void updateFilmTest() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        filmRepository.create(film);
        Assertions.assertEquals("testNameFilm", filmRepository.getById(film.getId()).getName());
        film.setName("NewName");
        filmRepository.update(film);
        Assertions.assertEquals("NewName", filmRepository.getById(film.getId()).getName());
    }

    @Test
    void deleteFilmTest() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        filmRepository.create(film);
        filmRepository.deleteFilmById(film.getId());
        AssertionsForClassTypes.assertThat(film).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void updateAndDeleteUnavailableFilmThrowsExceptionTest() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        AssertionsForClassTypes.assertThatThrownBy(() -> filmRepository.update(film))
                .isInstanceOf(NotFoundException.class);
        AssertionsForClassTypes.assertThatThrownBy(() -> filmRepository.deleteFilmById(1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void filmGetLike() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        filmRepository.create(film);

        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userRepository.create(user);

        filmRepository.addLike(film.getId(), user.getId());
        Assertions.assertEquals(1, filmRepository.getPopularFilms(10).size());
        Assertions.assertEquals("testNameFilm", filmRepository.getPopularFilms(10).get(0).getName());
    }

    @Test
    void filmDeleteLikeAndPopularFilmsReturnSize() {
        Film film = Film.builder()
                .name("testNameFilm").description("testDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        filmRepository.create(film);

        Film film2 = Film.builder()
                .name("testNameFilm2").description("testDesc2")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60).mpa(new Mpa(1, "G")).genres(null)
                .build();
        filmRepository.create(film2);

        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userRepository.create(user);

        User user2 = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userRepository.create(user2);

        filmRepository.addLike(film2.getId(), user.getId());
        filmRepository.addLike(film.getId(), user2.getId());
        filmRepository.addLike(film.getId(), user.getId());
        Assertions.assertEquals("testNameFilm", filmRepository.getPopularFilms(1).get(0).getName());

        filmRepository.deleteLike(film.getId(), user.getId());
        filmRepository.deleteLike(film.getId(), user2.getId());

        Assertions.assertEquals("testNameFilm2", filmRepository.getPopularFilms(1).get(0).getName());
    }
}
