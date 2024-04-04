package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.User;

import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository
public class FilmDbRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FilmDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addAndReturnId(Film film) {
        String sqlQuery = "insert into film (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa_id());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update film set " +
                "name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa_id()
                , film.getId());
    }

    @Override
    public boolean filmExists(Integer id) {
        String sqlQuery = "select count(*) from film where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into film_like (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sqlQuery = "SELECT * FROM film WHERE id IN ( SELECT film_id FROM film_like GROUP BY film_id " +
                "ORDER BY COUNT(user_id) DESC LIMIT ?);";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from film_like where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, Integer rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa_id(resultSet.getInt("mpa_id"))
                .build();
    }
//    @Override
//    public void add(Film film) {
//        String sqlQuery = "insert into film(name, description, releaseDate, duration, mpa_id) " +
//                "values (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sqlQuery,
//                film.getName(),
//                film.getDescription(),
//                film.getReleaseDate(),
//                film.getDuration(),
//                film.getMpa_id());
//    }
//
//    @Override
//    public Film getById(Integer id) {
//        String sqlQuery = "select * " +
//                "from film where id = ?";
//        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
//    }
//
//    @Override
//    public List<Film> getAll() {
//        String sqlQuery = "select * from film";
//        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
//    }
//
//    @Override
//    public void update(Film film) {
//        String sqlQuery = "update film set " +
//                "name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ?" +
//                "where id = ?";
//        jdbcTemplate.update(sqlQuery
//                , film.getName()
//                , film.getDescription()
//                , film.getReleaseDate()
//                , film.getDuration()
//                , film.getMpa_id()
//                , film.getId());
//    }
//
//    @Override
//    public boolean delete(Integer id) {
//        String sqlQuery = "delete from film where id = ?";
//        return jdbcTemplate.update(sqlQuery, id) > 0;
//    }
//
//    @Override
//    public boolean filmExists(Integer id) {
//        String sqlQuery = "select count(*) from film where id = ?";
//        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
//        return count != null && count > 0;
//    }
//
//    @Override
//    public void addLike(int filmId, int userId) {
//        String sqlQuery = "insert into film_like (film_id, user_id) " +
//                "values (?, ?)";
//        jdbcTemplate.update(sqlQuery,
//                filmId,
//                userId);
//    }
//
//    @Override
//    public List<Film> getPopular(int count) {
//        String sqlQuery = "SELECT * FROM film WHERE id IN ( SELECT film_id FROM film_like GROUP BY film_id " +
//                "ORDER BY COUNT(user_id) DESC LIMIT ?);";
//
//        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
//    }
//
//    @Override
//    public boolean deleteLike(int filmId, int userId) {
//        String sqlQuery = "delete from film_like where film_id = ? and user_id = ?";
//        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
//    }
//
//    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
//        return Film.builder()
//                .id(resultSet.getInt("id"))
//                .name(resultSet.getString("name"))
//                .description(resultSet.getString("description"))
//                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
//                .duration(resultSet.getInt("duration"))
//                .mpa_id(resultSet.getInt("mpa_id"))
//                .build();
//    }
}
