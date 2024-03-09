package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        log.info("Получен GET запрос к эндпоинту \"/user\".");
        return users.values();
    }

    public ResponseEntity<?> addUser(User user) {
        log.info("Получен POST запрос к эндпоинту \"/user\".");
        if (users.containsKey(user.getId())) {
            log.error("Уже существует пользователь с таким id");
            throw new UserAlreadyExistException(String.format("Уже существует пользователь с id [%s]", user.getId()));
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь с id = " + user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(User user) {
        log.info("Получен PUT запрос к эндпоинту \"/user\".");
        if (!users.containsKey(user.getId())) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user.getId()));
        }
        users.put(user.getId(), user);
        log.info("Обновлён пользователь с id = " + user.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public boolean userExists(Integer id) {
        return users.containsKey(id);
    }

    public User getUserById(Integer id) {
        return users.getOrDefault(id, null);
    }

    public Integer generateId() {
        return ++id;
    }
}