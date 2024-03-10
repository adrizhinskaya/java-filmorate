package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getUsers();

    public void addUser(User user);

    public void updateUser(User user);

    public boolean userExists(Integer id);

    public User getUserById(Integer id);
}