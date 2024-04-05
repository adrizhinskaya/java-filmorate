//package ru.yandex.practicum.filmorate.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import ru.yandex.practicum.filmorate.entity.Mpa;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class MpaDbRepositoryTest {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Test
//    public void testGetMpaById() {
//        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
//        Mpa savedMpa = mpaDbRepository.getById(1);
//
//        assertThat(savedMpa)
//                .isNotNull()
//                .usingRecursiveComparison()
//                .isEqualTo(Mpa.builder().id(1).name("G").build());
//    }
//
//    @Test
//    public void testGetMpaExists() {
//        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
//        boolean mpa1Exists = mpaDbRepository.mpaExists(1);
//        assertTrue(mpa1Exists);
//    }
//
//    @Test
//    public void testGetMpas() {
//        MpaDbRepository mpaDbRepository = new MpaDbRepository(jdbcTemplate);
//        Collection<Mpa> mpas = mpaDbRepository.getAll();
//
//        assertEquals(5, mpas.size());
//        assertTrue(mpas.contains(Mpa.builder().id(1).name("G").build()));
//    }
//}