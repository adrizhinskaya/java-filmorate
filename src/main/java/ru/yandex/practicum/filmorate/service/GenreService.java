package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
        log.info("Получен GET запрос к эндпоинту \"/mpa\".");
        return genreDbRepository.getAll();
    }

    public Genre getById(Integer id) {
        log.info(String.format("Получен GET запрос [%S] к эндпоинту \"/mpa/{id}\".", id));
        if (!genreDbRepository.genreExists(id)) {
            log.error("Не существует жанра с таким id");
            throw new UserNotFoundException(String.format("Жанр с id [%s] не найден.", id));
        }
        return genreDbRepository.getById(id);
    }
}