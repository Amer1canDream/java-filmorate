package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) throws ValidateException {
        return service.create(user);
    }

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) throws ValidateException {
        return service.update(user);
    }

    @GetMapping("/users")
    public Collection getUsers() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") int id) {
        return service.findById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id1, @PathVariable("friendId") int id2) {
        service.addFriend(id1, id2);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int id1, @PathVariable("friendId") int id2) {
        service.removeFriend(id1, id2);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return service.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id1, @PathVariable("otherId") int id2) {
        return service.getCommonFriends(id1, id2);
    }
}
