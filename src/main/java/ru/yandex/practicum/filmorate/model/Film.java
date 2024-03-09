package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NotBefore1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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

    private Set<Integer> likes;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
}