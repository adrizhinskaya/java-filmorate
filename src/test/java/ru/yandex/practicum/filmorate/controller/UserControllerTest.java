package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

class UserControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void userCreate() {
        User user = new User(null, "yandex@mail.ru", "test_user", "Test User", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assert violations.isEmpty();
    }

    @Test
    public void userCreateBlankEmail() {
        User user = new User(1, " ", "test_user", "Test User", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assert !violations.isEmpty();
    }

    @Test
    public void userCreateFailEmail() {
        User user = new User(1, "invalid_email", "test_user", "Test User", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assert !violations.isEmpty();
    }

    @Test
    public void userCreateFailLogin() {
        User user = new User(1, "yandex@mail.ru", "", "Test User", LocalDate.of(2000, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assert !violations.isEmpty();
    }

    @Test
    public void userCreateFailBirthday() {
        User user = new User(1, "test@example.com", "test_user", "Test User", LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assert !violations.isEmpty();
    }
}
