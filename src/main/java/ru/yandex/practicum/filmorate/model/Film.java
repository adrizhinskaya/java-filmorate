package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validator.NotBefore1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Film {
    private Integer id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotBefore1895
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
