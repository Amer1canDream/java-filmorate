package ru.yandex.practicum.filmorate.storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Primary
@Slf4j
@Qualifier("DbFilmStorage")
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private int id = 0;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("FILM_ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE"));
        film.setDuration(resultSet.getInt("DURATION"));
        film.setMpa(new Rating(resultSet.getInt("RATING_ID"), resultSet.getString("R_NAME")));
        return film;
    }

    @Override
    public Film findById(int id) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS f JOIN RATINGS r ON f.RATING_ID = r.RATING_ID " +
                        "WHERE f.FILM_ID = ?";
        List<Film> result = jdbcTemplate.query(sql, this::mapToFilm, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<Film> getFilms() {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS f " +
                        "LEFT OUTER JOIN ratings AS r ON f.rating_id = r.rating_id " +
                        "ORDER BY f.film_id";
        return jdbcTemplate.query(sql, this::mapToFilm);
    }

    public List<Film> findAllByYear(int year) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS f JOIN RATINGS r ON f.RATING_ID = r.RATING_ID " +
                        "WHERE YEAR(f.RELEASE_DATE) = ? ORDER BY f.FILM_ID";
        return jdbcTemplate.query(sql, this::mapToFilm, year);
    }

    public List<Film> findAllByGenre(int genreId) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS f JOIN RATINGS r ON f.RATING_ID = r.RATING_ID " +
                        "WHERE f.FILM_ID IN (SELECT FILMS_GENRES.FILM_ID FROM FILMS_GENRES WHERE GENRE_ID = ?) ORDER BY f.FILM_ID";
        return jdbcTemplate.query(sql, this::mapToFilm, genreId);
    }

    public List<Film> findAllByGenreAndYear(int genreId, int year) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS f JOIN RATINGS r ON f.RATING_ID = r.RATING_ID " +
                        "WHERE f.FILM_ID IN (SELECT FILMS_GENRES.FILM_ID FROM FILMS_GENRES WHERE GENRE_ID = ?) " +
                        "AND YEAR(f.RELEASE_DATE) = ? ORDER BY f.FILM_ID";
        return jdbcTemplate.query(sql, this::mapToFilm, genreId, year);
    }

    @Override
    public void create(Film film) throws ValidateException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");
        if (film.getReleaseDate().before(d2)) {
            log.info("Date must be after 1895-01-28");
            throw new ValidateException("Date must be after 1895-01-28");
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("RATING_ID", film.getMpa().getId());


        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
    }

    @Override
    public void update(Film film) throws ParseException, ValidateException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");

        if (film.getReleaseDate().before(d2)) {
            log.info("Date must be after 1895-01-28");
            throw new ValidateException("Date must be after 1895-01-28");
        }

        Film searchedFilm = findById(film.getId());
        if (searchedFilm == null) {
            log.info("There are no films with this id");
            throw new NotFoundException("There are no films with this id");
        }
        String sql =
                "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? " +
                        "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
    }

    public void delete(int id) {
        final String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void saveLikes(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS_LIKES WHERE FILM_ID = ?", film.getId());

        String sql = "INSERT INTO FILMS_LIKES (FILM_ID, USER_ID) VALUES(?, ?)";
        Set<Integer> likes = film.getLikes();
        for (var like : likes) {
            jdbcTemplate.update(sql, film.getId(), like);
        }
    }

    public void loadLikes(Film film) {
        String sql = "SELECT USER_ID FROM FILMS_LIKES WHERE FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()) {
            film.addLike(sqlRowSet.getInt("USER_ID"));
        }
    }

    public void createGenresByFilm(Film film) {
        String sql = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES(?, ?)";

        Set<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        ArrayList<Integer> genresId = new ArrayList<>();
        for (var genre : genres) {
            if (genresId.contains(genre.getId())) {
                break;
            } else {
                genresId.add(genre.getId());
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }

    public void updateGenresByFilm(Film film) {
        String sql = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
        createGenresByFilm(film);
    }

    public List<Film> commonMovies(Long userId, Long friendId) {
        String sql =
                "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.NAME R_NAME " +
                        "FROM FILMS_LIKES fl1 JOIN FILMS_LIKES fl2 ON " +
                        "fl1.FILM_ID  = fl2.FILM_ID " +
                        "AND fl1.USER_ID  != fl2.USER_ID " +
                        "AND fl1.USER_ID = ? " +
                        "AND fl2.USER_ID = ? " +
                        "JOIN FILMS f ON fl1.FILM_ID = f.FILM_ID " +
                        "JOIN RATINGS r ON f.RATING_ID = r.RATING_ID ORDER BY f.FILM_ID";
        List<Film> films = jdbcTemplate.query(sql, this::mapToFilm, userId, friendId);
        return films;
    }

    private void setId(Film film) {
        id++;
        film.setId(id);
    }
}
