package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        log.info("Получен GET запрос к эндпоинту \"/film\".");
        return films.values();
    }

    public ResponseEntity<?> addFilm(Film film) {
        log.info("Получен POST запрос к эндпоинту \"/film\".");
        if (films.containsKey(film.getId())) {
            log.error("Уже существует фильм с таким id");
            throw new FilmAlreadyExistException(String.format("Уже существует фильм с id [%s]", film.getId()));
        }

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм с id = " + film.getId());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public ResponseEntity<?> updateFilm(Film film) {
        log.info("Получен PUT запрос к эндпоинту \"/film\".");
        if (!films.containsKey(film.getId())) {
            log.error("Не существует фильма с таким id");
            throw new FilmNotFoundException(String.format("Фильм с id [%s] не найден.", film.getId()));
        }
        films.put(film.getId(), film);
        log.info("Обновлён фильм с id = " + film.getId());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }


    public boolean filmExists(Integer id) {
        return films.containsKey(id);
    }

    public Film getFilmById(Integer id) {
        return films.getOrDefault(id, null);
    }

    public Integer generateId() {
        return ++id;
    }
}