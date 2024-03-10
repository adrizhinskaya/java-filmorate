package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public void addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    public boolean userExists(Integer id) {
        return users.containsKey(id);
    }

    public User getUserById(Integer id) {
        return users.getOrDefault(id, null);
    }

    private Integer generateId() {
        return ++id;
    }
}