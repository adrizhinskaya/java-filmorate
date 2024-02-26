package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получен GET запрос к эндпоинту \"/user\".");
        return users.values();
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        log.info("Получен POST запрос к эндпоинту \"/user\".");
        if (users.containsKey(user.getId())) {
            log.error("Уже существует пользователь с таким id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь с id = " + user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту \"/user\".");
        if (!users.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        users.put(user.getId(), user);
        log.info("Обновлён пользователь с id = " + user.getId());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Ошибка валидации к эндпоинту \"/user\": {}", errors);
        return errors;
    }

    private Integer generateId() {
        return id++;
    }
}
