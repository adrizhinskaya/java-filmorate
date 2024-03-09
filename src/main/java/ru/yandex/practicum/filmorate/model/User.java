package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private Set<Integer> friends;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
        friends = new HashSet<>();
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }
}