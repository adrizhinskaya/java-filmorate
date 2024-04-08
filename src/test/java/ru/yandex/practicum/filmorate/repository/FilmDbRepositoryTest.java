package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class FilmDbRepositoryTest {
    private final JdbcTemplate jdbcTemplate;
    private final Mpa mpa = Mpa.builder().name("G").id(1).build();
    private final Genre genre1 = Genre.builder().name("Комедия").id(1).build();
    private final Genre genre2 = Genre.builder().name("Драма").id(2).build();

    @Test
    public void testGetFilmById() {
        Collection<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        Film film = new Film(1, "film1", "description1",
                LocalDate.of(1990, 1, 1), 101, mpa, genres);

        FilmDbRepository filmDbRepository = new FilmDbRepository(jdbcTemplate);
        filmDbRepository.addAndReturnId(film);
        genres.clear();
        genres.add(genre2);
        Film newFilm = new Film(1, "film2", "description2",
                LocalDate.of(1990, 1, 2), 102, mpa, genres);
        filmDbRepository.update(newFilm);

        Film savedFilm = filmDbRepository.getById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testGetFilms() {
        Collection<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        Film film1 = new Film(1, "film1", "description1",
                LocalDate.of(1990, 1, 1), 101, mpa, genres);
        Film film2 = new Film(2, "film2", "description2",
                LocalDate.of(1990, 1, 2), 102, mpa, genres);

        FilmDbRepository filmDbRepository = new FilmDbRepository(jdbcTemplate);
        filmDbRepository.addAndReturnId(film1);
        filmDbRepository.addAndReturnId(film2);

        Collection<Film> films = filmDbRepository.getAll();
        boolean film1Exists = filmDbRepository.filmExists(film1.getId());

        assertTrue(film1Exists);
        assertEquals(2, films.size());
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
    }

    @Test
    public void testGetPopularFilms() {
        User user = new User(1, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        UserDbRepository userDbRepository = new UserDbRepository(jdbcTemplate);
        userDbRepository.addAndReturnId(user);

        Collection<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        Film film1 = new Film(1, "film1", "description1",
                LocalDate.of(1990, 1, 1), 101, mpa, genres);
        Film film2 = new Film(2, "film2", "description2",
                LocalDate.of(1990, 1, 2), 102, mpa, genres);
        FilmDbRepository filmDbRepository = new FilmDbRepository(jdbcTemplate);
        filmDbRepository.addAndReturnId(film1);
        filmDbRepository.addAndReturnId(film2);
        filmDbRepository.addLike(2, 1);
        Collection<Film> popFilms = filmDbRepository.getPopular(100);

        assertEquals(2, popFilms.size());
    }
}