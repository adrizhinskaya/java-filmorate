package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.repository.MpaDbRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

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
        log.info("Получен GET запрос к эндпоинту \"/mpa\".");
        return mpaDbRepository.getAll();
    }

    public Mpa getById(Integer id) {
        log.info(String.format("Получен GET запрос [%S] к эндпоинту \"/mpa/{id}\".", id));
        if(!mpaDbRepository.mpaExists(id)) {
            log.error("Не существует mpa с таким id");
            throw new UserNotFoundException(String.format("Mpa с id [%s] не найден.", id));
        }
        return mpaDbRepository.getById(id);
    }
}
