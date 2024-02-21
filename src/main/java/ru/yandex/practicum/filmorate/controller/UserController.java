package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
