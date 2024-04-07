package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.*;
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
        log.info("POST \"/films\".");
        mpaExsistsCheck(film.getMpa().getId());
        genreExsistsCheck(film.getGenres());
        Integer id = filmRepository.addAndReturnId(film);
        film.setId(id);
        log.info(String.format("Создан фильм id=[%s] и жанрами [%s]", film.getId(), film.getGenres()));
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public Film getFilmById(Integer id) {
        log.info("POST \"/films/{id}.");
        Film film = filmRepository.getById(id);
        log.info(String.format("Получен фильм id=[%s] с жанрами [%s]", film.getId(), film.getGenres()));
        return film;
    }

    public Collection<Film> getFilms() {
        log.info("GET \"/films\".");
        Collection<Film> films = filmRepository.getAll();
        log.info(String.format("Получены фильмы [ %s ]", films));
        return films;
    }

    public ResponseEntity<?> updateFilm(Film film) {
        log.info("PUT \"/films\".");
        filmExsistsCheck(film.getId());
        mpaExsistsCheck(film.getMpa().getId());
        genreExsistsCheck(film.getGenres());
        filmRepository.update(film);
        log.info(String.format("Обновлён фильм id=[%s]", film.getId()));

        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public ResponseEntity<?> addLike(int filmId, int userId) {
        log.info("PUT \"/films/{id}/like/{userId}\".");
        userExsistsCheck(userId);
        filmExsistsCheck(filmId);
        filmRepository.addLike(filmId, userId);
        log.info(String.format("Добавлен лайк фильму id=[%s] от пользователя id=[%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Collection<Film> getPopular(int count) {
        log.info("GET \"/films/popular?count={count}\".");
        Collection<Film> films = filmRepository.getAll();
        if (count <= 0 || count > films.size()) {
            count = 10;
        }
        Collection<Film> popFilms = filmRepository.getPopular(count);
        log.info(String.format("Получены фильмы [ %s ]", popFilms));
        return popFilms;
    }

    public ResponseEntity<?> deleteLike(int filmId, int userId) {
        log.info("DELETE \"/films/{id}/like/{userId}\".");
        userExsistsCheck(userId);
        filmExsistsCheck(filmId);
        filmRepository.deleteLike(filmId, userId);
        log.info(String.format("Удалён лайк фильму id=[%s] от пользователя id=[%s]", filmId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void filmExsistsCheck(int id) {
        if (!filmRepository.filmExists(id)) {
            log.error("Не существует фильма с таким id");
            throw new FilmNotFoundException(String.format("Фильм id=[%s] не найден.", id));
        }
    }

    private void userExsistsCheck(int id) {
        if (!userRepository.userExists(id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь id=[%s] не найден.", id));
        }
    }

    private void mpaExsistsCheck(int id) {
        if (!mpaDbRepository.mpaExists(id)) {
            log.error("Не существует mpa с таким id");
            throw new MpaBadRequestException(String.format("Mpa id=[%s] не найден.", id));
        }
    }

    private void genreExsistsCheck(Collection<Genre> genres) {
        if (genres != null) {
            for (Genre g : genres) {
                if (!genreDbRepository.genreExists(g.getId())) {
                    log.error("Не существует genre с таким id");
                    throw new GenreBadRequestException(String.format("Жанр id=[%s] не найден.", g.getId()));
                }
            }
        }
    }
}