package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.NotBefore1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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

    private Integer mpa_id;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Integer mpa_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa_id = mpa_id;
    }
}