package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int id = 0;
    private HashMap<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    @Override
    public void create(User user) throws ValidateException {
        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contains probel");
        }

        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException("Birthday can not be in future");
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        setId(user);
        users.put(user.getId(), user);
        log.info("User {} created", user.getName());
    }

    @Override
    public void update(User user) throws ValidateException {
        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contains probel");
        }
        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            log.info("Birthday can not be in future");
            throw new ValidateException("Birthday can not be in future");
        }
        if (!users.containsKey(user.getId())) {
            log.info("There is no user with this ID");
            throw new ValidateException("There is no user with this ID");
        }
        users.put(user.getId(), user);
        log.info("User {} updated", user.getName());
    }

    @Override
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<User>(users.values());
        return usersList;
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    private void setId(User user) {
        id++;
        user.setId(id);
    }
}
