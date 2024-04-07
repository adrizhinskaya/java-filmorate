package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.sql.*;
import java.util.Collection;

@Repository
public class FilmDbRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbRepository mpaDbRepository;
    private final GenreDbRepository genreDbRepository;

    @Autowired
    public FilmDbRepository(JdbcTemplate jdbcTemplate, MpaDbRepository mpaDbRepository,
                            GenreDbRepository genreDbRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbRepository = mpaDbRepository;
        this.genreDbRepository = genreDbRepository;
    }

    @Override
    public Integer addAndReturnId(Film film) {
        String sqlQuery = "insert into film (name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        if (film.getGenres() != null) {
            jdbcTemplate.batchUpdate("merge into film_genre (film_id, genre_id) key (film_id, genre_id) values (?, ?)",
                    film.getGenres(),
                    100,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, id);
                        ps.setInt(2, genre.getId());
                    });
        }
        return id;
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "select * from film where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update film set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            jdbcTemplate.batchUpdate("update film_genre set genre_id = ? where film_id = ?",
                    film.getGenres(),
                    100,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, genre.getId());
                        ps.setInt(2, film.getId());
                    });
        }
    }

    @Override
    public boolean filmExists(Integer id) {
        String sqlQuery = "select count(*) from film where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into film_like (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        String sqlQuery = "select f.* from film f left outer join (select film_id, count(user_id) as like_count " +
                "from film_like group by film_id) fl on f.id = fl.film_id " +
                "order by coalesce(fl.like_count, 0) desc limit ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from film_like where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, Integer rowNum) throws SQLException {
        Collection<Genre> genres = (getFilmGenres(resultSet.getInt("id")));

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(resultSet.getString("mpa_id") != null ?
                        mpaDbRepository.getById(resultSet.getInt("mpa_id")) : null)
                .genres(genres)
                .build();
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        String sqlQuery = "select * from film_genre where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre, filmId);
    }

    private Genre mapRowToFilmGenre(ResultSet resultSet, Integer rowNum) throws SQLException {
        return genreDbRepository.getById(resultSet.getInt("genre_id"));
    }
}