package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
public class Film {
    @Positive
    private int id;
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
