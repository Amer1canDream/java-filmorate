package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@Slf4j
@Qualifier("DbUserStorage")
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private int id = 0;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        List<User> result = jdbcTemplate.query(sql, this::mapToUser, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    private User mapToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("USER_ID"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setName(resultSet.getString("NAME"));
        user.setBirthday(resultSet.getDate("BIRTHDAY"));
        return user;
    }

    public void loadFriends(User user) {
        String sql =
                "(SELECT USER_ID2 ID FROM FRIENDSHIP  WHERE USER_ID1 = ?) " +
                        "UNION " +
                        "(SELECT USER_ID1 ID FROM FRIENDSHIP  WHERE USER_ID2 = ? AND  CONFIRMED = true)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId(), user.getId());
        while (sqlRowSet.next()) {
            user.addFriend(sqlRowSet.getInt("id"));
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM USERS ORDER BY USER_ID";
        return jdbcTemplate.query(sql, this::mapToUser);
    }

    @Override
    public User create(User user) throws ValidateException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");

        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contains probel");
        }

        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            throw new ValidateException("Birthday can not be in future");
        }

        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        setId(user);
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("NAME", user.getName());
        values.put("BIRTHDAY", user.getBirthday());
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User update(User user) throws ValidateException {
        String sql =
                "UPDATE USERS SET LOGIN = ?, EMAIL = ?, NAME = ?, BIRTHDAY = ?" +
                        "WHERE USER_ID = ?";
        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login can not contain whitespaces");
        }
        if (user.getBirthday().after(Date.valueOf(LocalDate.now()))) {
            log.info("Birthday can not be in future");
            throw new ValidateException("Birthday can not be in future");
        }

        if (findById(user.getId()) == null) {
            log.info("There is no user with this ID");
            throw new NotFoundException("There is no user with this ID");
        }
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean containsEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, email);
        return filmRows.next();
    }

    @Override
    public boolean containsFriendship(int filterId1, int filterId2, Boolean filterConfirmed) {
        String sql = "SELECT * FROM FRIENDSHIP WHERE USER_ID1 = ? AND USER_ID2 = ? AND  CONFIRMED = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filterId1, filterId2, filterConfirmed);
        return rows.next();
    }

    @Override
    public void updateFriendship(int id1, int id2, boolean confirmed, int filterId1, int filterId2) {
        String sql =
                "UPDATE FRIENDSHIP SET USER_ID1 = ?, USER_ID2 = ?, CONFIRMED = ? " +
                        "WHERE USER_ID1 = ? AND USER_ID2 = ?";
        jdbcTemplate.update(sql, id1, id2, confirmed, filterId1, filterId2);
    }

    public void insertFriendship(int id, int friendId) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID1, USER_ID2, CONFIRMED) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, false);
    }

    public void removeFriendship(int filterId1, int filterId2) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID1 = ? AND USER_ID2 = ?";
        jdbcTemplate.update(sql, filterId1, filterId2);
    }

    public List<Long> getUsersFilms(int userId) {
        String sql = "SELECT film_id FROM films_likes WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"), userId);
    }

    private void setId(User user) {
        id++;
        user.setId(id);
    }
}
