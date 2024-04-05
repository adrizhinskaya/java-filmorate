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
        String sqlQuery = "insert into film (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";

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
            String sqlQuery1 = "merge into film_genre (film_id, genre_id) key (film_id, genre_id) " +
                    "values (?, ?)";

            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(sqlQuery1, id, g.getId());
            }
        }
        return id;
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "select * " +
                "from film where id = ?";
        Film a = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        return a;
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update film set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            String sqlQuery1 = "update film_genre set " +
                    "genre_id = ?" +
                    "where film_id = ?";

            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(sqlQuery1, g.getId(), film.getId());
            }
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
        String sqlQuery = "insert into film_like (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        String sqlQuery = "SELECT f.* FROM film f LEFT OUTER JOIN " +
                "(SELECT film_id, COUNT(user_id) AS like_count FROM film_like GROUP BY film_id) fl " +
                "ON f.id = fl.film_id ORDER BY COALESCE(fl.like_count, 0) DESC LIMIT ?";

        Collection<Film> pop = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        return pop;
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from film_like where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, Integer rowNum) throws SQLException {
        Collection<Genre> genres = (getFilmGenres(resultSet.getInt("id")));

        Film result = Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(resultSet.getString("mpa_id") != null ?
                        mpaDbRepository.getById(resultSet.getInt("mpa_id")) : null)
                .genres(genres)
                .build();
        return result;
    }

    private Collection<Genre> getFilmGenres(Integer filmId) {
        String sqlQuery = "select * from film_genre " +
                "where film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre, filmId);
    }

    private Genre mapRowToFilmGenre(ResultSet resultSet, Integer rowNum) throws SQLException {
        return genreDbRepository.getById(resultSet.getInt("genre_id"));
    }
}