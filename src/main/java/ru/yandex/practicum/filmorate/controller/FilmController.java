package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam int count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }
}
