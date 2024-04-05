package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreDbRepository;
import ru.yandex.practicum.filmorate.repository.MpaDbRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaDbRepository mpaDbRepository;
    private final GenreDbRepository genreDbRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository, UserRepository userRepository, MpaDbRepository mpaDbRepository,
                       GenreDbRepository genreDbRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.mpaDbRepository = mpaDbRepository;
        this.genreDbRepository = genreDbRepository;
    }

    public ResponseEntity<?> addFilm(Film film) {
        if (!mpaDbRepository.mpaExists(film.getMpa().getId())) {
            log.error("Не существует mpa с таким id");
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                if (!genreDbRepository.genreExists(g.getId())) {
                    log.error("Не существует genre с таким id");
                    return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
                }
            }
        }

        Integer id = filmRepository.addAndReturnId(film);
        film.setId(id);
        log.info(String.format("Добавлен фильм с id = [%s] и жанрами [%s]", film.getId(), film.getGenres()));
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public Film getFilmById(Integer id) {
        Film film = filmRepository.getById(id);
        log.info(String.format("GET фильм с id = [%s] и жанрами [%s]", film.getId(), film.getGenres()));
        return film;
    }

    public Collection<Film> getFilms() {
        log.info("Получен GET запрос к эндпоинту \"/film\".");
        return filmRepository.getAll();
    }

    public ResponseEntity<?> updateFilm(Film film) {
        log.info("Получен PUT запрос к эндпоинту \"/film\".");
        if (!filmRepository.filmExists(film.getId())) {
            log.error("Не существует фильма с таким id");
            throw new FilmNotFoundException(String.format("Фильм с id [%s] не найден.", film.getId()));
        }
        if (!mpaDbRepository.mpaExists(film.getMpa().getId())) {
            log.error("Не существует mpa с таким id");
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                if (!genreDbRepository.genreExists(g.getId())) {
                    log.error("Не существует genre с таким id");
                    return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
                }
            }
        }
        log.error("Проверки пройдены");
        filmRepository.update(film);
        log.info("Обновлён фильм с id = " + film.getId());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public ResponseEntity<?> addLike(int filmId, int userId) {
        log.info("Получен PUT запрос к эндпоинту \"/films/{id}/like/{userId}\".");
        if (!userRepository.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!filmRepository.filmExists(filmId)) {
            log.error("Не существует фильма с таким id");
            throw new UserNotFoundException(String.format("Фильм с id [%s] не найден.", filmId));
        }
        filmRepository.addLike(filmId, userId);
        log.info(String.format("Добавлен лайк фильму с id [%s] от пользователя с id [%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Collection<Film> getPopular(int count) {
        log.info("Получен GET запрос к эндпоинту \"/films/popular?count={count}\".");
        Collection<Film> films = filmRepository.getAll();
        if (count <= 0 || count > films.size()) {
            count = 10;
        }

        return filmRepository.getPopular(count);
    }

    public ResponseEntity<?> deleteLike(int filmId, int userId) {
        log.info("Получен DELETE запрос к эндпоинту \"/films/{id}/like/{userId}\".");
        if (!userRepository.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!filmRepository.filmExists(filmId)) {
            log.error("Не существует фильма с таким id");
            throw new UserNotFoundException(String.format("Фильм с id [%s] не найден.", filmId));
        }
        filmRepository.deleteLike(filmId, userId);
        log.info(String.format("Удалён лайк фильму с id [%s] от пользователя с id [%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}