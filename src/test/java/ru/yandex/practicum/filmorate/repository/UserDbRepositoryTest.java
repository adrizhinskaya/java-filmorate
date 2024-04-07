//package ru.yandex.practicum.filmorate.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import ru.yandex.practicum.filmorate.entity.User;
//
//import java.time.LocalDate;
//import java.util.Collection;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@JdbcTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class UserDbRepositoryTest {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void clearSequence() {
//        jdbcTemplate.execute("ALTER SEQUENCE serial RESTART WITH 0;");
//    }
//
//    @After
//    public void resetSequence() {
//        jdbcTemplate.execute("ALTER SEQUENCE serial RESTART WITH 0;");
//    }
//
//    @Test
//    public void testGetUserById() {
//        User user = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
//        User newUser = new User(1, "user@email.ru", "Newvanya123", "NewIvan Petrov", LocalDate.of(1990, 1, 1));
//
//        UserDbRepository userDbRepository = new UserDbRepository(jdbcTemplate);
//        userDbRepository.addAndReturnId(user);
//        userDbRepository.update(newUser);
//
//        User savedUser = userDbRepository.getById(1);
//
//        assertThat(savedUser)
//                .isNotNull()
//                .usingRecursiveComparison()
//                .isEqualTo(newUser);
//    }
//
//    @Test
//    public void testGetUsers() {
//        User user1 = new User(1, "user1@email.ru", "vanya1", "Ivan Petrov1", LocalDate.of(1990, 1, 1));
//        User user2 = new User(2, "user2@email.ru", "vanya2", "Ivan Petrov2", LocalDate.of(1990, 1, 2));
//
//        UserDbRepository userDbRepository = new UserDbRepository(jdbcTemplate);
//        userDbRepository.addAndReturnId(user1);
//        userDbRepository.addAndReturnId(user2);
//
//        Collection<User> users = userDbRepository.getAll();
//        boolean user1Exists = userDbRepository.userExists(user1.getId());
//
//        assertTrue(user1Exists);
//        assertEquals(2, users.size());
//        assertTrue(users.contains(user1));
//        assertTrue(users.contains(user2));
//    }
//
//    @Test
//    public void testGetFriends() {
//        User user1 = new User(1, "user1@email.ru", "vanya1", "Ivan Petrov1", LocalDate.of(1990, 1, 1));
//        User user2 = new User(2, "user2@email.ru", "vanya2", "Ivan Petrov2", LocalDate.of(1990, 1, 2));
//
//        UserDbRepository userDbRepository = new UserDbRepository(jdbcTemplate);
//        userDbRepository.addAndReturnId(user1);
//        userDbRepository.addAndReturnId(user2);
//        userDbRepository.addFriend(user1.getId(), user2.getId());
//        Collection<User> friends = userDbRepository.getFriends(user1.getId());
//
//        assertEquals(1, friends.size());
//        assertTrue(friends.contains(user2));
//
//        userDbRepository.deleteFriend(user1.getId(), user2.getId());
//        friends = userDbRepository.getFriends(user1.getId());
//
//        assertEquals(0, friends.size());
//    }
//
//    @Test
//    public void testGetCommonFriends() {
//        User user1 = new User(1, "user1@email.ru", "vanya1", "Ivan Petrov1", LocalDate.of(1990, 1, 1));
//        User user2 = new User(2, "user2@email.ru", "vanya2", "Ivan Petrov2", LocalDate.of(1990, 1, 2));
//        User user3 = new User(3, "user3@email.ru", "vanya3", "Ivan Petrov3", LocalDate.of(1990, 1, 3));
//
//        UserDbRepository userDbRepository = new UserDbRepository(jdbcTemplate);
//        userDbRepository.addAndReturnId(user1);
//        userDbRepository.addAndReturnId(user2);
//        userDbRepository.addAndReturnId(user3);
//        userDbRepository.addFriend(user1.getId(), user3.getId());
//        userDbRepository.addFriend(user2.getId(), user3.getId());
//        Collection<User> commonFriends = userDbRepository.getCommonFriends(user1.getId(), user2.getId());
//
//        assertEquals(1, commonFriends.size());
//        assertTrue(commonFriends.contains(user3));
//    }
//}