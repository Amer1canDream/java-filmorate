package ru.yandex.practicum.filmorate.db_impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.storage.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmDbStorageTest {
    private final DbFilmStorage filmStorage;
    private final GenreStorage genreStorage;

    @Test
    void findById() throws ValidateException, ParseException {
        Film expFilm = getExpFilm1();
        filmStorage.create(expFilm);
        Film actFilm = filmStorage.findById(expFilm.getId());
        assertEquals(expFilm.getId(), actFilm.getId());
        assertEquals(expFilm.getName(), actFilm.getName());
    }

    @Test
    void findAll() throws ValidateException, ParseException {
        Film expFilm1 = getExpFilm1();
        filmStorage.create(expFilm1);
        Film expFilm2 = getExpFilm2();
        filmStorage.create(expFilm2);
        List<Film> expFilms = List.of(expFilm1, expFilm2);

        List<Film> actFilms = filmStorage.getFilms();
        assertEquals(2, actFilms.size());
    }

    @Test
    void create() throws ValidateException, ParseException {
        Film expFilm = getExpFilm1();
        filmStorage.create(expFilm);
        filmStorage.updateGenresByFilm(expFilm);
        Film actFilm = filmStorage.findById(expFilm.getId());
        actFilm.setGenres(genreStorage.getGenresByFilm(actFilm));
        assertEquals(expFilm.getId(),actFilm.getId());
        assertEquals(expFilm.getName(),actFilm.getName());
        assertEquals(expFilm.getDescription(),actFilm.getDescription());
        assertEquals(expFilm.getReleaseDate(),actFilm.getReleaseDate());
        assertEquals(expFilm.getDuration(),actFilm.getDuration());
        assertEquals(expFilm.getMpa().getId(),actFilm.getMpa().getId());
        assertEquals(expFilm.getGenres().size(),actFilm.getGenres().size());
    }

    @Test
    void update() throws ValidateException, ParseException {
        Film expFilm = getExpFilm1();
        filmStorage.create(expFilm);
        expFilm.setName("Super Film");

        filmStorage.update(expFilm);
        Film actFilm = filmStorage.findById(expFilm.getId());

        assertEquals(expFilm.getId(), actFilm.getId());
        assertEquals(expFilm.getName(), actFilm.getName());
    }

    private Film getExpFilm1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse("2020-03-3");
        Film film = new Film();
        film.setId(1);
        film.setName("Film1");
        film.setDescription("DESCRIPTION1");
        film.setReleaseDate(d1);
        film.setDuration(100);

        Rating rating = new Rating();
        rating.setId(1);
        film.setMpa(rating);

        Genre genre1 = new Genre();
        genre1.setId(1);
        Genre genre2 = new Genre();
        genre2.setId(2);
        film.setGenres(Set.of(genre1, genre2));
        return film;
    }

    private Film getExpFilm2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("2010-01-3");
        Film film = new Film();
        film.setId(2);
        film.setName("Film2");
        film.setDescription("DESCRIPTION2");
        film.setReleaseDate(d2);
        film.setDuration(90);
        Rating rating = new Rating();
        rating.setId(2);
        film.setMpa(rating);
        return film;
    }
}
