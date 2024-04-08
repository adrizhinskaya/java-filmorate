package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class GenreDbRepositoryTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetGenreById() {
        GenreDbRepository genreDbRepository = new GenreDbRepository(jdbcTemplate);
        Genre savedGenre = genreDbRepository.getById(1);

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(Genre.builder().id(1).name("Комедия").build());
    }

    @Test
    public void testGetGenreExists() {
        GenreDbRepository genreDbRepository = new GenreDbRepository(jdbcTemplate);
        boolean genre1Exists = genreDbRepository.genreExists(1);
        assertTrue(genre1Exists);
    }

    @Test
    public void testGetGenres() {
        GenreDbRepository genreDbRepository = new GenreDbRepository(jdbcTemplate);
        Collection<Genre> genres = genreDbRepository.getAll();

        assertEquals(6, genres.size());
        assertTrue(genres.contains(Genre.builder().id(1).name("Комедия").build()));
    }
}