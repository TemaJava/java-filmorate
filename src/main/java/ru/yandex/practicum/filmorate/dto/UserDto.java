package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@With
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    int id;

    @Email(message = "Email is incorrect")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Login is required")
    String login;

    String name;

    @NotNull(message = "Birthday is required")
    @PastOrPresent(message = "Birthday must not be later than the current date")
    LocalDate birthday;
}