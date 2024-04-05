package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.User;

import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDbRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer addAndReturnId(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public User getById(Integer id) {
        String sqlQuery = "select * " +
                "from users where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public boolean userExists(Integer id) {
        String sqlQuery = "select count(*) from users where id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "insert into users_friend (user_id, friend_id, status) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId,
                true);
    }

    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM users u " +
                "JOIN users_friend uf ON u.id = uf.friend_id " +
                "WHERE uf.user_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT fr1.friend_id FROM users_friend AS fr1 " +
                "JOIN users_friend AS fr2 ON fr1.friend_id = fr2.friend_id " +
                "WHERE fr1.user_id = ? AND fr2.user_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, user1Id, user2Id);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM users_friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, Integer rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}