package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.Collection;

public interface FilmRepository {
    public Integer addAndReturnId(Film film);

    public Film getById(Integer id);

    public Collection<Film> getAll();

    public void update(Film film);

    public boolean filmExists(Integer id);

    public void addLike(Integer filmId, Integer userId);

    public Collection<Film> getPopular(Integer count);

    public boolean deleteLike(Integer filmId, Integer userId);
}