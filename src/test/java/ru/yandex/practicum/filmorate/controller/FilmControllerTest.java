package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

class FilmControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void filmCreate() {
        Film film = new Film(null, "Test Film", "Test_film description", LocalDate.of(2000, 1, 1), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert violations.isEmpty();
    }

    @Test
    public void filmCreateFailName() {
        Film film = new Film(null, " ", "Test_film description", LocalDate.of(2000, 1, 1), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailDescription() {
        Film film = new Film(null, "Test Film", "01234567890123456789012345678901234567890123456789" +
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901234567891",
                LocalDate.of(2000, 1, 1), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailReleaseDate() {
        Film film = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(1890, 3, 25), 200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailDuration() {
        Film film = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), -200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }
}
