package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public void create(Film film) throws ValidateException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = sdf.parse("1895-01-28");
        if (film.getReleaseDate().before(d2)) {
            log.info("Date must be after 1895-01-28");
            throw new ValidateException("Date must be after 1895-01-28");
        }
        setId(film);
        films.put(film.getId(), film);
        log.info("Film {} created", film.getName());
    }

    @Override
    public void update(Film film) throws ValidateException, ParseException {
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
    }

    @Override
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<Film>(films.values());
        return filmsList;
    }

    @Override
    public Film findById(int id) {
        return films.get(id);
    }

    @Override
    public void saveLikes(Film film) throws ValidateException, ParseException {
        update(film);
    }

    @Override
    public void createGenresByFilm(Film film) {

    }

    @Override
    public void loadLikes(Film film) {

    }

    @Override
    public void updateGenresByFilm(Film film) {

    }

    private void setId(Film film) {
        id++;
        film.setId(id);
    }
}
