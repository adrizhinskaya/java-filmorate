//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import ru.yandex.practicum.filmorate.model.User;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import java.time.LocalDate;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class UserControllerTest {
//    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//
//    @Test
//    public void userCreateExisting() {
//        UserController controller = mock(UserController.class);
//        User existingUser = new User(null, "yandex@mail.ru", "test_user", "Test User",
//                LocalDate.of(2000, 1, 1));
//        existingUser.setId(1);
//        when(controller.addUser(existingUser)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
//        ResponseEntity<?> result = controller.addUser(existingUser);
//        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test
//    public void userUpdateNew() {
//        UserController controller = mock(UserController.class);
//        User newUser = new User(null, "yandex@mail.ru", "test_user", "Test User",
//                LocalDate.of(2000, 1, 1));
//        newUser.setId(1);
//        when(controller.updateUser(newUser)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        ResponseEntity<?> result = controller.updateUser(newUser);
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//
//    @Test
//    public void userCreate() {
//        User user = new User(null, "yandex@mail.ru", "test_user", "Test User",
//                LocalDate.of(2000, 1, 1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assert violations.isEmpty();
//    }
//
//    @Test
//    public void userCreateBlankEmail() {
//        User user = new User(1, " ", "test_user", "Test User",
//                LocalDate.of(2000, 1, 1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assert !violations.isEmpty();
//    }
//
//    @Test
//    public void userCreateFailEmail() {
//        User user = new User(1, "invalid_email", "test_user", "Test User",
//                LocalDate.of(2000, 1, 1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assert !violations.isEmpty();
//    }
//
//    @Test
//    public void userCreateFailLogin() {
//        User user = new User(1, "yandex@mail.ru", "", "Test User",
//                LocalDate.of(2000, 1, 1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assert !violations.isEmpty();
//    }
//
//    @Test
//    public void userCreateFailBirthday() {
//        User user = new User(1, "test@example.com", "test_user", "Test User",
//                LocalDate.now().plusDays(1));
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assert !violations.isEmpty();
//    }
//}
