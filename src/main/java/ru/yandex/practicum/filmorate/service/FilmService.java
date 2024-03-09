package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getPopular(int count) {
        log.info("Получен GET запрос к эндпоинту \"/films/popular?count={count}\".");
        Collection<Film> films = filmStorage.getFilms();
        if (count <= 0 || count > films.size()) {
            count = 10;
        }
        List<Film> sortedFilms = films.stream()
                .sorted(Comparator.comparingInt(value -> value.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        Collections.reverse(sortedFilms);
        return sortedFilms;
    }

    public ResponseEntity<?> addLike(int filmId, int userId) {
        log.info("Получен PUT запрос к эндпоинту \"/films/{id}/like/{userId}\".");
        if (!userStorage.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!filmStorage.filmExists(filmId)) {
            log.error("Не существует фильма с таким id");
            throw new UserNotFoundException(String.format("Фильм с id [%s] не найден.", filmId));
        }
        filmStorage.getFilmById(filmId).addLike(userId);
        log.info(String.format("Добавлен лайк фильму с id [%s] от пользователя с id [%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteLike(int filmId, int userId) {
        log.info("Получен DELETE запрос к эндпоинту \"/films/{id}/like/{userId}\".");
        if (!userStorage.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!filmStorage.filmExists(filmId)) {
            log.error("Не существует фильма с таким id");
            throw new UserNotFoundException(String.format("Фильм с id [%s] не найден.", filmId));
        }
        filmStorage.getFilmById(filmId).deleteLike(userId);
        log.info(String.format("Удалён лайк фильму с id [%s] от пользователя с id [%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}