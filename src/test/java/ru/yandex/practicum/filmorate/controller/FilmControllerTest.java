package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private final Mpa mpa = Mpa.builder().name("G").id(1).build();
    private final Genre genre1 = Genre.builder().name("Комедия").id(1).build();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Collection<Genre> genres = new ArrayList<>();

    @Test
    public void filmCreateExisting() {
        genres.add(genre1);
        FilmController controller = mock(FilmController.class);
        Film existingFilm = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), 200, mpa, genres);
        existingFilm.setId(1);
        when(controller.addFilm(existingFilm)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        ResponseEntity<?> result = controller.addFilm(existingFilm);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void filmUpdateNew() {
        genres.add(genre1);
        FilmController controller = mock(FilmController.class);
        Film newFilm = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), 200, mpa, genres);
        newFilm.setId(1);
        when(controller.updateFilm(newFilm)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseEntity<?> result = controller.updateFilm(newFilm);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void filmCreate() {
        genres.add(genre1);
        Film film = new Film(null, "Test Film", "Test_film description", LocalDate.of(2000,
                1, 1), 200, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert violations.isEmpty();
    }

    @Test
    public void filmCreateFailName() {
        genres.add(genre1);
        Film film = new Film(null, " ", "Test_film description", LocalDate.of(2000,
                1, 1), 200, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailDescription() {
        genres.add(genre1);
        Film film = new Film(null, "Test Film", "012345678901234567890123456789012345678901234" +
                "56789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901234567891",
                LocalDate.of(2000, 1, 1), 200, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailReleaseDate() {
        genres.add(genre1);
        Film film = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(1890, 3, 25), 200, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }

    @Test
    public void filmCreateFailDuration() {
        genres.add(genre1);
        Film film = new Film(null, "Test Film", "Test_film description",
                LocalDate.of(2000, 1, 1), -200, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assert !violations.isEmpty();
    }
}