package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriends(int id) {
        log.info("Получен GET запрос к эндпоинту \"/users/{id}/friends\".");
        if (!userStorage.userExists(id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", id));
        }
        User user = userStorage.getUserById(id);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        log.info("Получен GET запрос к эндпоинту \"/users/{id}/friends/common/{otherId}\".");
        if (!userStorage.userExists(user1Id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user1Id));
        }
        if (!userStorage.userExists(user2Id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user2Id));
        }
        User user1 = userStorage.getUserById(user1Id);
        User user2 = userStorage.getUserById(user2Id);
        return user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> addFriend(int userId, int friendId) {
        log.info("Получен PUT запрос к эндпоинту \"/users/{id}/friends/{friendId}\".");
        if (!userStorage.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!userStorage.userExists(friendId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", friendId));
        }
        userStorage.getUserById(userId).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(userId);
        log.info("Добавлен новый друг с id = " + friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteFriend(int userId, int friendId) {
        log.info("Получен DELETE запрос к эндпоинту \"/users/{id}/friends/{friendId}\".");
        if (!userStorage.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!userStorage.userExists(friendId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", friendId));
        }
        userStorage.getUserById(userId).deleteFriend(friendId);
        log.info("Удалён друг с id = " + friendId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }
}
