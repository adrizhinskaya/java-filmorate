package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.repository.MpaDbRepository;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {
    private final MpaDbRepository mpaDbRepository;

    @Autowired
    public MpaService(MpaDbRepository mpaDbRepository) {
        this.mpaDbRepository = mpaDbRepository;
    }

    public Collection<Mpa> getAll() {
        log.info("GET \"/mpa\".");
        Collection<Mpa> mpas = mpaDbRepository.getAll();
        log.info(String.format("Получены mpa [ %s ]", mpas));
        return mpas;
    }

    public Mpa getById(Integer id) {
        log.info("GET \"/mpa/{id}\".");
        if (!mpaDbRepository.mpaExists(id)) {
            log.error("Не существует mpa с таким id");
            throw new MpaNotFoundException(String.format("Mpa id=[%s] не найден.", id));
        }
        return mpaDbRepository.getById(id);
    }
}