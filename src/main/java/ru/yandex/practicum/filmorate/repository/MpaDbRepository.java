package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class MpaDbRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    public Mpa getById(Integer id) {
        String sqlQuery = "select * from mpa where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    public boolean mpaExists(Integer id) {
        String sqlQuery = "select count(*) from mpa where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, Integer rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}