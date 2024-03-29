package ru.yandex.practicum.filmorate.db_impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserDbStorageTest {
    private final DbUserStorage userStorage;
    private static final String EMAIL1 = "user1@ya.ru";
    private static final String EMAIL2 = "user2@ya.ru";

    @Test
    void findById() throws ValidateException, ParseException {
        User expUser = getExpUser1();
        userStorage.create(expUser);
        User actUser = userStorage.findById(expUser.getId());
        assertEquals(expUser.getId(), actUser.getId());
        assertEquals(expUser.getName(), actUser.getName());
    }

    @Test
    void findAll() throws ValidateException, ParseException {
        User expUser1 = getExpUser1();
        userStorage.create(expUser1);
        User expUser2 = getExpUser2();
        userStorage.create(expUser2);
        List<User> expUsers = List.of(expUser1, expUser2);

        List<User> actUsers = userStorage.getUsers();
        assertEquals(expUsers, actUsers);
        assertEquals(2, actUsers.size());
    }

    @Test
    void create() throws ValidateException, ParseException {
        User expUser = getExpUser1();
        userStorage.create(expUser);
        User actUser = userStorage.findById(expUser.getId());
        assertEquals(expUser.getId(),actUser.getId());
        assertEquals(expUser.getName(),actUser.getName());
        assertEquals(expUser.getEmail(), actUser.getEmail());
        assertEquals(expUser.getLogin(), actUser.getLogin());
        assertEquals(expUser.getBirthday(), actUser.getBirthday());
    }

    @Test
    void update() throws ValidateException, ParseException {
        User expUser = getExpUser1();
        userStorage.create(expUser);
        expUser.setName("Super User");

        userStorage.update(expUser);
        User actUser = userStorage.findById(expUser.getId());

        assertEquals(expUser.getId(), actUser.getId());
        assertEquals(expUser.getName(), actUser.getName());
    }

    @Test
    void containsEmail() throws ValidateException, ParseException {
        User expUser1 = getExpUser1();
        userStorage.create(expUser1);
        assertTrue(userStorage.containsEmail(EMAIL1));
        assertFalse(userStorage.containsEmail(EMAIL2));
    }

    @Test
    void addFriend() throws ValidateException, ParseException {
        User expUser1 = getExpUser1();
        userStorage.create(expUser1);
        User expUser2 = getExpUser2();
        userStorage.create(expUser2);

        List<Integer> actFriends1 = expUser1.getFiends();
        assertTrue(actFriends1.isEmpty());
        expUser1.addFriend(expUser2.getId());
        actFriends1 = expUser1.getFiends();
        assertEquals(expUser2.getId(), actFriends1.get(0).intValue());

        List<Integer> actFriends2 = expUser2.getFiends();
        assertTrue(actFriends2.isEmpty());
        expUser2.addFriend(expUser1.getId());
        actFriends2 = expUser2.getFiends();
        assertEquals(expUser1.getId(),actFriends2.get(0).intValue());
    }

    @Test
    void removeFriend() throws ValidateException, ParseException {
        User expUser1 = getExpUser1();
        userStorage.create(expUser1);
        User expUser2 = getExpUser2();
        userStorage.create(expUser2);

        expUser1.addFriend(expUser2.getId());
        expUser2.addFriend(expUser1.getId());
        assertFalse(expUser1.getFiends().isEmpty());
        assertFalse(expUser2.getFiends().isEmpty());

        expUser1.removeFriend(expUser2.getId());
        assertTrue(expUser1.getFiends().isEmpty());
        assertFalse(expUser2.getFiends().isEmpty());

        expUser2.removeFriend(expUser1.getId());
        assertTrue(expUser1.getFiends().isEmpty());
        assertTrue(expUser2.getFiends().isEmpty());
    }

    private User getExpUser1() throws ParseException {
        User user = new User();
        user.setEmail(EMAIL1);
        user.setLogin("usr1");
        user.setName("User1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("1987-19-1");
        user.setBirthday(d1);
        return user;
    }

    private User getExpUser2() throws ParseException {
        User user = new User();
        user.setEmail(EMAIL2);
        user.setLogin("usr2");
        user.setName("User2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1990-05-6");
        user.setBirthday(d2);
        return user;
    }
}
