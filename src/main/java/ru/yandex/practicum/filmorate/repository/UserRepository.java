package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {
    public Integer addAndReturnId(User user);

    public User getById(Integer id);

    public Collection<User> getAll();

    public void update(User user);

    public boolean userExists(Integer id);

    public void addFriend(Integer userId, Integer friendId);

    public Collection<User> getFriends(Integer id);

    public Collection<User> getCommonFriends(Integer user1Id, Integer user2Id);

    public void deleteFriend(Integer userId, Integer friendId);
}