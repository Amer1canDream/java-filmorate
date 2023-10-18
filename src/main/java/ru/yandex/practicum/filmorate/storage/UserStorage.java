package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {

    User create(User user) throws ValidateException;

    User update(User user) throws ValidateException;

    List<User> getUsers();

    User findById(int id);

    boolean containsFriendship(int filterId1, int filterId2, Boolean filterConfirmed);

    void updateFriendship(int id1, int id2, boolean confirmed, int filterId1, int filterId2);

    void insertFriendship(int id, int friendId);

    void removeFriendship(int filterId1, int filterId2);

    List<Long> getUsersFilms(int userId);

    void loadFriends(User user);
}
