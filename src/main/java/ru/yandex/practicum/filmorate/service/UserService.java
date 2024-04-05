package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addUser(User user) {
        log.info("Получен POST запрос к эндпоинту \"/user\".");

        Integer id = userRepository.addAndReturnId(user);
        user.setId(id);
        log.info("Добавлен новый пользователь с id = " + user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public Collection<User> getUsers() {
        log.info("Получен GET запрос к эндпоинту \"/user\".");
        return userRepository.getAll();
    }

    public ResponseEntity<?> updateUser(User user) {
        log.info("Получен PUT запрос к эндпоинту \"/user\".");
        if (!userRepository.userExists(user.getId())) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user.getId()));
        }

        userRepository.update(user);
        log.info("Обновлён пользователь с id = " + user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> addFriend(int userId, int friendId) {
        log.info("Получен PUT запрос к эндпоинту \"/users/{id}/friends/{friendId}\".");
        if (!userRepository.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!userRepository.userExists(friendId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", friendId));
        }

        userRepository.addFriend(userId, friendId);
        log.info(String.format("Пользователю с id [%s] добавлен новый друг с id [%s] ", userId, friendId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<User> getFriends(int id) {
        log.info(String.format("Получен GET запрос [%S] к эндпоинту \"/users/{id}/friends\".", id));
        if (!userRepository.userExists(id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", id));
        }
        return userRepository.getFriends(id);
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        log.info("Получен GET запрос к эндпоинту \"/users/{id}/friends/common/{otherId}\".");
        if (!userRepository.userExists(user1Id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user1Id));
        }
        if (!userRepository.userExists(user2Id)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", user2Id));
        }

        return userRepository.getCommonFriends(user1Id, user2Id);
    }

    public ResponseEntity<?> deleteFriend(int userId, int friendId) {
        log.info("Получен DELETE запрос к эндпоинту \"/users/{id}/friends/{friendId}\".");
        if (!userRepository.userExists(userId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", userId));
        }
        if (!userRepository.userExists(friendId)) {
            log.error("Не существует пользователя с таким id");
            throw new UserNotFoundException(String.format("Пользователь с id [%s] не найден.", friendId));
        }

        userRepository.deleteFriend(userId, friendId);
        log.info("Удалён друг с id = " + friendId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }
}