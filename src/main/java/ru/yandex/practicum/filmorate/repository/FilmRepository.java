package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.entity.Film;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public interface FilmRepository {
    public Integer addAndReturnId(Film film);
    public List<Film> getAll();
    public void update(Film film);
    public boolean filmExists(Integer id);
    public void addLike(Integer filmId, Integer userId);
    public List<Film> getPopular(Integer count);
    public boolean deleteLike(Integer filmId, Integer userId);
}