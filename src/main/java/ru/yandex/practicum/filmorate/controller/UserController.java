package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private int id = 0;
    private List users = new ArrayList();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidateException {
        if (user.getEmail().isBlank()) {
            throw new ValidateException("Email can not be null");
        }

        if (user.getEmail() == null) {
            throw new ValidateException("Email can not be null");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidateException("User email must contains @");
        }

        if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidateException("Login can not be null");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contains probel");
        }

        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException("Birthday can not be in future");
        }

        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        setId(user);
        users.add(user);
        log.info("User {} created", user.getName());
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidateException {
        if (user.getEmail().isBlank()) {
            throw new ValidateException("Email can not be null");
        }

        if (user.getEmail() == null) {
            throw new ValidateException("Email can not be null");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidateException("User email must contains @");
        }

        if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidateException("Login can not be null");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contains probel");
        }

        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException("Birthday can not be in future");
        }
        if (!users.contains(user)) {
            throw new ValidateException("There is no user with this ID");
        }
        users.add(user);
        log.info("User {} updated", user.getName());
        return user;
    }

    @GetMapping("/users")
    public List getUsers() {
        log.info("All users returned");
        return users;
    }

    private void setId(User user) {
        id++;
        user.setId(id);
    }
}
