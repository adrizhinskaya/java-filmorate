package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void filmCreateExisting() {
        FilmController controller = mock(FilmController.class);
        Film existingFilm = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), 200);
        existingFilm.setId(1);
        when(controller.addFilm(existingFilm)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        ResponseEntity<?> result = controller.addFilm(existingFilm);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void filmUpdateNew() {
        FilmController controller = mock(FilmController.class);
        Film newFilm = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), 200);
        newFilm.setId(1);
        when(controller.updateFilm(newFilm)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseEntity<?> result = controller.updateFilm(newFilm);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

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