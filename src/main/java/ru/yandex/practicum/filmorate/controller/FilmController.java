package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import java.text.ParseException;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
public class FilmController {
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws ValidateException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");

        if (film.getName().isEmpty()) {
            throw new ValidateException("Name can not be null");
        }
        if (film.getName() == null) {
            throw new ValidateException("Name can not be null");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Length of description can't be more that 200");
        }
        if (film.getReleaseDate().before(d2)) {
            throw new ValidateException("Date must be after 1895-01-28");
        }
        if (film.getDuration() < 0) {
            throw new ValidateException("Duration must be positive");
        }
        setId(film);
        films.put(film.getId(), film);
        log.info("Film {} created", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidateException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");

        if (film.getName().isEmpty()) {
            throw new ValidateException("Name can not be null");
        }
        if (film.getName() == null) {
            throw new ValidateException("Name can not be null");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Length of description can't be more that 200");
        }
        if (film.getReleaseDate().before(d2)) {
            throw new ValidateException("Date must be after 1895-01-28");
        }
        if (film.getDuration() < 0) {
            throw new ValidateException("Duration must be positive");
        }
        films.put(film.getId(), film);
        log.info("Film {} updated", film.getName());
        return film;
    }

    @GetMapping("/films")
    public HashMap<Integer, Film> getFilms() {
        log.info("All films returned");
        return films;
    }

    private void setId(Film film) {
        id++;
        film.setId(id);
    }
}
