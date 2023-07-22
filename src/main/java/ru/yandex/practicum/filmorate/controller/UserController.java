package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidateException {
        if ( user.getEmail().isBlank() ) {
            throw new ValidateException( "Email can not be null");
        }

        if ( user.getEmail() == null  ) {
            throw new ValidateException( "Email can not be null");
        }

        if ( !user.getEmail().contains("@") ) {
            throw new ValidateException( "User email must contains @");
        }

        if ( user.getLogin().isBlank() || user.getLogin() == null ) {
            throw new ValidateException( "Login can not be null" );
        }

        if ( user.getLogin().contains(" ")) {
            throw new ValidateException( "Login can not contains probel" );
        }

        if ( user.getName().isBlank() || user.getName() == null ) {
            user.setName(user.getLogin());
        }

        if ( user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException( "Birthday can not be in future");
        }
        users.put(user.getId(), user);
        log.info("User {} created", user.getName());
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidateException {
        if ( user.getEmail().isBlank() ) {
            throw new ValidateException( "Email can not be null");
        }

        if ( user.getEmail() == null  ) {
            throw new ValidateException( "Email can not be null");
        }

        if ( !user.getEmail().contains("@") ) {
            throw new ValidateException( "User email must contains @");
        }

        if ( user.getLogin().isBlank() || user.getLogin() == null ) {
            throw new ValidateException( "Login can not be null" );
        }

        if ( user.getLogin().contains(" ")) {
            throw new ValidateException( "Login can not contains probel" );
        }

        if ( user.getName().isBlank() || user.getName() == null ) {
            user.setName(user.getLogin());
        }

        if ( user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException( "Birthday can not be in future");
        }
        users.put(user.getId(), user);
        log.info("User {} updated", user.getName());
        return user;
    }

    @GetMapping("/users")
    public HashMap<Integer, User> getUsers() {
        log.info("All users returned");
        return users;
    }
}
