package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import java.text.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) throws ValidateException, ParseException {
        service.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) throws ValidateException, ParseException {
        service.removeLike(id, userId);
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) throws ValidateException, ParseException {
        return service.create(film);
    }
    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) throws ValidateException, ParseException {
        service.update(film);
        return film;
    }
    @GetMapping("/films")
    public Collection getFilms() {
        return service.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmsById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findPopularMovies(
            @RequestParam(defaultValue = "10") int count) {
        return service.findPopularMovies(count);
    }
}
