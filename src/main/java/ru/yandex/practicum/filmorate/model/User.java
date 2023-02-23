package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @PositiveOrZero
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    //убрали поле друзей, потому что теперь информация хранится в бд
}
