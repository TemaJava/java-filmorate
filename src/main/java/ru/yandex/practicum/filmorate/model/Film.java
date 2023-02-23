package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Mpa mpa;
    private List<Genre> genres;
    //убрали поле лайков, потому что теперь информация хранится в бд
    //компаратор тоже не нужен благодаря запросам
}
