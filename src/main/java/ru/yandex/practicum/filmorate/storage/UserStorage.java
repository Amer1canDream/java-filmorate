package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;

public interface UserStorage {

    void create(User user) throws ValidateException;

    void update(User user) throws ValidateException;

    Collection getUsers();

    User findById(int id);
}
