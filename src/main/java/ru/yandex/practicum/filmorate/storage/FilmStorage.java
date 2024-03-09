package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getFilms();

    public ResponseEntity<?> addFilm(Film film);

    public ResponseEntity<?> updateFilm(Film film);
    public boolean filmExists(Integer id);

    public Film getFilmById(Integer id);

    public Integer generateId();}
