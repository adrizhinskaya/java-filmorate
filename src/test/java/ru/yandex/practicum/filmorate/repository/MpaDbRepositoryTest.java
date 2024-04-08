package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class MpaDbRepositoryTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetMpaById() {
        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
        Mpa savedMpa = mpaDbRepository.getById(1);

        assertThat(savedMpa)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(Mpa.builder().id(1).name("G").build());
    }

    @Test
    public void testGetMpaExists() {
        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
        boolean mpa1Exists = mpaDbRepository.mpaExists(1);
        assertTrue(mpa1Exists);
    }

    @Test
    public void testGetMpas() {
        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
        Collection<Mpa> mpas = mpaDbRepository.getAll();

        assertEquals(5, mpas.size());
        assertTrue(mpas.contains(Mpa.builder().id(1).name("G").build()));
    }
}