package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class GenreDbRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Genre> getAll() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getById(Integer id) {
        String sqlQuery = "select * from genre where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    public boolean genreExists(Integer id) {
        String sqlQuery = "select count(*) from genre where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    private Genre mapRowToGenre(ResultSet resultSet, Integer rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}