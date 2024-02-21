package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validator.DurationPositive;
import ru.yandex.practicum.filmorate.validator.NotBefore1895;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotBefore1895
    private LocalDate releaseDate;

    @DurationPositive
    private Duration duration;
}
