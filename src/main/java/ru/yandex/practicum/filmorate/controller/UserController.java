package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        log.info("Получен POST запрос к эндпоинту \"/user\".");
        return user;
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        log.info("Получен PUT запрос к эндпоинту \"/user\".");
        return user;
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
}
