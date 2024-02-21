package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/film")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
