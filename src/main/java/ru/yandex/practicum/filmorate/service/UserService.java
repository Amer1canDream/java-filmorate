package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private int id = 0;
    protected final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) throws ValidateException {
        storage.create(user);
        return user;
    }

    public User update(User user) throws ValidateException {
        storage.update(user);
        return user;
    }

    public Collection getUsers() {
        Collection users = storage.getUsers();
        return users;
    }

    public User findById(int id) {
        User user = storage.findById(id);
        if (user == null) {
            String message = ("User not found");
            log.warn(message);
            throw new NotFoundException(message);
        } else {
            return user;
        }
    }

    public void addFriend(int id, int friendId) {
        User user = this.findById(id);
        User friend = this.findById(friendId);
        if (user == null || friend == null) {
            String message = ("User not found");
            log.warn(message);
            throw new NotFoundException(message);
        }
        if (user.containsFriend(friendId)) {
            log.warn("User frineds contains frinedId");
            return;
        }
        user.addFriend(friendId);

        if (friend.containsFriend(id)) {
            log.warn("Friend frineds contains userId");
            return;
        }
        friend.addFriend(id);
    }

    public void removeFriend(int id, int friendId) {
        User user = this.findById(id);
        User friend = this.findById(friendId);
        if (user == null || friend == null) {
            String message = ("User not found");
            log.warn(message);
            throw  new NotFoundException(message);
        }

        if (user.containsFriend(friendId)) {
            user.removeFriend(friendId);
        }
        if (friend.containsFriend(id)) {
            friend.removeFriend(id);
        }
    }

    public List<User> getFriends(int id) {
        User user = this.findById(id);
        if (user == null) {
            String message = ("User not found");
            log.warn(message);
            throw new NotFoundException(message);
        }
        List<Integer> friendsId = user.getFiends();
        List<User> friends = new ArrayList<>();
        for (var friendId : friendsId) {
            friends.add(this.findById(friendId));
        }

        return friends;
    }

    public List<User> getCommonFriends(int id1, int id2) {
        User user1 = this.findById(id1);
        User user2 = this.findById(id2);
        if (user1 == null || user2 == null) {
            String message = ("Пользователь не найден");
            log.warn(message);
            throw  new NotFoundException(message);
        }
        List<Integer> friendsId1 = user1.getFiends();
        List<Integer> friendsId2 = user2.getFiends();
        friendsId1.retainAll(friendsId2);

        List<User> friends = new ArrayList<>();
        for (var friendId : friendsId1) {
            friends.add(this.findById(friendId));
        }

        return friends;
    }

}
