package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class DbRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public  DbRatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Rating findById(int id) {
        String sql = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<Rating> result = jdbcTemplate.query(sql, this::mapToRating, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    private Rating mapToRating(ResultSet resultSet, int rowNum) throws SQLException {
        Rating rating = new Rating();
        rating.setId(resultSet.getInt("RATING_ID"));
        rating.setName(resultSet.getString("NAME"));
        return rating;
    }

    public List<Rating> findAll() {
        String sql = "SELECT * FROM RATINGS ORDER BY RATING_ID";
        return jdbcTemplate.query(sql, this::mapToRating);
    }

    public Rating create(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("RATINGS")
                .usingGeneratedKeyColumns("RATING_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", rating.getName());

        rating.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return rating;
    }

    public Rating update(Rating rating) {
        String sql = "UPDATE RATINGS SET NAME = ? WHERE RATING_ID = ?";
        jdbcTemplate.update(sql, rating.getName(), rating.getId());
        return rating;
    }

    public void delete(int id) {
        final String sql = "DELETE FROM ratings WHERE rating_id = ?";
        jdbcTemplate.update(sql, id);
    }
}