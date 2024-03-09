package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getUsers();

    public ResponseEntity<?> addUser(User user);

    public ResponseEntity<?> updateUser(User user);

    public boolean userExists(Integer id);

    public User getUserById(Integer id);

    public Integer generateId();
}
