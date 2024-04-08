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

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addUser(User user) {
        log.info("POST \"/user\".");
        Integer id = userRepository.addAndReturnId(user);
        user.setId(id);
        log.info(String.format("Создан пользователь id=[%s]", user.getId()));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public Collection<User> getUsers() {
        log.info("GET \"/user\".");
        Collection<User> users = userRepository.getAll();
        log.info(String.format("Получены пользователи [ %s ]", users));
        return users;
    }

    public ResponseEntity<?> updateUser(User user) {
        log.info("PUT \"/user\".");
        userExsistsCheck(user.getId());
        userRepository.update(user);
        log.info(String.format("Обновлён пользователь id=[%s]", user.getId()));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> addFriend(int userId, int friendId) {
        log.info("PUT \"/users/{id}/friends/{friendId}\".");
        userExsistsCheck(userId);
        userExsistsCheck(friendId);
        userRepository.addFriend(userId, friendId);
        log.info(String.format("Добавлен друг id[%s] пользователю id[%s] ", friendId, userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Collection<User> getFriends(int id) {
        log.info("GET \"/users/{id}/friends\".");
        userExsistsCheck(id);
        Collection<User> friends = userRepository.getFriends(id);
        log.info(String.format("Получены пользователи [ %s ]", friends));
        return friends;
    }

    public Collection<User> getCommonFriends(int user1Id, int user2Id) {
        log.info("GET \"/users/{id}/friends/common/{otherId}\".");
        userExsistsCheck(user1Id);
        userExsistsCheck(user2Id);
        Collection<User> friends = userRepository.getCommonFriends(user1Id, user2Id);
        log.info(String.format("Получены пользователи [ %s ]", friends));
        return friends;
    }

    public ResponseEntity<?> deleteFriend(int userId, int friendId) {
        log.info("DELETE \"/users/{id}/friends/{friendId}\".");
        userExsistsCheck(userId);
        userExsistsCheck(friendId);
        userRepository.deleteFriend(userId, friendId);
        log.info("Удалён друг id = " + friendId);
        log.info(String.format("Удалён друг id[%s] пользователя id[%s] ", friendId, userId));
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    public void userExsistsCheck(int userId) {
        if (!userRepository.userExists(userId)) {
            log.info(String.format("Не существует пользователя с id[%s] ", userId));
            throw new UserNotFoundException(String.format("Пользователь id=[%s] не найден.", userId));
        }
    }
}