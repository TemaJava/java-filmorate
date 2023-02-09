package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collections;
import java.util.Optional;

@Component
public class FilmMapper implements RawMapper<FilmDto, Film> {
    @Override
    public Film mapFrom(FilmDto dto) {
        return Film.builder().id(dto.getId()).name(dto.getName()).description(dto.getDescription())
                .releaseDate(dto.getReleaseDate()).duration(dto.getDuration()).mpa(dto.getMpa())
                .genres(Optional.ofNullable(dto.getGenres()).orElse(Collections.emptyList()))
                .build();
    }
}
