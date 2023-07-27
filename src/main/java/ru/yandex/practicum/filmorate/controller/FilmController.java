package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import java.text.ParseException;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

@RestController
public class FilmController {
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public ResponseEntity postFilm(@Valid @RequestBody Film film) throws ValidateException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");
        if (film.getReleaseDate().before(d2)) {
            log.info("Date must be after 1895-01-28");
            throw new ValidateException("Date must be after 1895-01-28");
        }
        setId(film);
        films.put(film.getId(), film);
        log.info("Film {} created", film.getName());
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) throws ValidateException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");

        if (film.getReleaseDate().before(d2)) {
            log.info("Date must be after 1895-01-28");
            throw new ValidateException("Date must be after 1895-01-28");
        }
        if (!films.containsKey(film.getId())) {
            log.info("There are no films with this id");
            throw new ValidateException("There are no films with this id");
        }
        films.put(film.getId(), film);
        log.info("Film {} updated", film.getName());
        return film;
    }

    @GetMapping("/films")
    public Collection getFilms() {
        return films.values();
    }

    private void setId(Film film) {
        id++;
        film.setId(id);
    }
}
