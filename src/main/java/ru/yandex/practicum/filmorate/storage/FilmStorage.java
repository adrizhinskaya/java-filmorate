package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public void addFilm(Film film);

    public void updateFilm(Film film);

    public boolean filmExists(Integer id);

    public Film getFilmById(Integer id);
}