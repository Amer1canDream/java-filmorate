package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class User {
    private int id;

    public User(int id, String name, String email, String login, Date birthday) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private final Set<Integer> friends = new HashSet<>();

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private final Set<Integer> films = new HashSet<>();

    public User() {

    }

    public void addFriend(int id) {
        friends.add(id);
    }

    public void removeFriend(int id) {
        friends.remove(id);
    }

    public List<Integer> getFiends() {
        return new ArrayList<>(friends);
    }

    public boolean containsFriend(int id) {
        return friends.contains(id);
    }
}
