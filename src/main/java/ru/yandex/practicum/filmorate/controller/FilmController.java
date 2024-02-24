package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/film")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        log.info("Получен POST запрос к эндпоинту \"/film\".");
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        log.info("Получен PUT запрос к эндпоинту \"/film\".");
        return film;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Ошибка валидации к эндпоинту \"/film\": {}", errors);
        return errors;
    }
}
