package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NotBefore1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
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

    private Mpa mpa;
    private Collection<Genre> genres;
//                "rate": 4,
//                        "mpa": { "id": 5},
//            "genres": [{ "id": 2}]

}