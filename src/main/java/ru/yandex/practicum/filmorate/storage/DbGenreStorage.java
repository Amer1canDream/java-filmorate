package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
@Qualifier("DbGenreStorage")
public class DbGenreStorage implements GenreStorage, Comparator<Genre> {
    private int id = 0;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findById(int id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> result = jdbcTemplate.query(sql, this::mapToGenre, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    private Genre mapToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("NAME"));
        return genre;
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID ";
        return jdbcTemplate.query(sql, this::mapToGenre);
    }

    public Genre create(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("GENRES")
                .usingGeneratedKeyColumns("GENRE_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", genre.getName());

        genre.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return genre;
    }

    public Genre update(Genre genre) {
        String sql = "UPDATE GENRES SET NAME = ? WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public int compare(Genre o1, Genre o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }

    @Override
    public Set<Genre> getGenresByFilm(Film film) {
        String sql = "SELECT g.GENRE_ID, g.NAME FROM GENRES g NATURAL JOIN FILMS_GENRES fg WHERE fg.FILM_ID = ? ORDER BY g.GENRE_ID";
        Set<Genre> genresSet = new TreeSet<>(this::compare);
        for (Genre genre: jdbcTemplate.query(sql, this::mapToGenre, film.getId())) {
            genresSet.add(genre);
        }
        return genresSet;
    }
}
