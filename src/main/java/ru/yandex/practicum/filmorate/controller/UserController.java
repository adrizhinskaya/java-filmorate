package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }
    @PostMapping
    public User addUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
    @PutMapping
    public User updateUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
