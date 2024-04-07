package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.repository.GenreDbRepository;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreDbRepository genreDbRepository;

    @Autowired
    public GenreService(GenreDbRepository genreDbRepository) {
        this.genreDbRepository = genreDbRepository;
    }

    public Collection<Genre> getAll() {
        log.info("GET \"/mpa\".");
        Collection<Genre> genres = genreDbRepository.getAll();
        log.info(String.format("Получены жанры [ %s ]", genres));
        return genres;
    }

    public Genre getById(Integer id) {
        log.info("GET \"/mpa/{id}\".");
        if (!genreDbRepository.genreExists(id)) {
            log.error("Не существует жанра с таким id");
            throw new GenreNotFoundException(String.format("Жанр id=[%s] не найден.", id));
        }
        return genreDbRepository.getById(id);
    }
}