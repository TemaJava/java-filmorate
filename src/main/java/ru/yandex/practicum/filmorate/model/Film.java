package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@With
@Data
@Builder
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private List<Integer> likes;
    private List<Genre> genres;
    @NotNull
    private Mpa mpa;

    public static final Comparator<Film> COMPARE_BY_COUNT = Comparator.comparingInt(o -> o.getLikes().size());
}
