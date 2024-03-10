package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        return films.values();
    }

    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    public boolean filmExists(Integer id) {
        return films.containsKey(id);
    }

    public Film getFilmById(Integer id) {
        return films.getOrDefault(id, null);
    }

    private Integer generateId() {
        return ++id;
    }
}