package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private int id = 0;
    @Qualifier("DbFilmStorage")
    protected final FilmStorage filmStorage;
    @Qualifier("DbGenreStorage")
    protected final GenreStorage genreStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, GenreStorage genreStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) throws ValidateException, ParseException {
        filmStorage.create(film);
        filmStorage.updateGenresByFilm(film);
        return film;
    }

    public Film update(Film film) throws ValidateException, ParseException {
        filmStorage.update(film);
        filmStorage.updateGenresByFilm(film);
        return film;
    }

    public Collection getFilms() {
        List<Film> films = filmStorage.getFilms();
        films.forEach(this::loadData);
        return films;
    }
    private void loadData(Film film) {
        film.setGenres(genreStorage.getGenresByFilm(film));
        filmStorage.loadLikes(film);
    }
    public Film findById(int id) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            String message = ("Film not found");
            log.warn(message);
            throw new NotFoundException(message);
        }
        loadData(film);
        return film;
    }

    public void addLike(int id, int userId) throws ValidateException, ParseException {
        Film film = this.findById(id);
        User user = userStorage.findById(userId);
        if (film == null || user == null) {
            String message = ("Film or user not found");
            log.warn(message);
            throw  new NotFoundException(message);
        }
        film.addLike(userId);
        filmStorage.saveLikes(film);
    }

    public void removeLike(int id, int userId) throws ValidateException, ParseException {
        Film film = this.findById(id);
        User user = userStorage.findById(userId);
        if (film == null || user == null) {
            String message = ("Film or user not found");
            log.warn(message);
            throw  new NotFoundException(message);
        }
        film.removeLike(userId);
        filmStorage.saveLikes(film);
    }

    public List<Film> findPopularMovies(int count) {
        List<Film> films = (List<Film>) this.getFilms();
        films.sort(Comparator.comparing(Film::getLikesCount).reversed());
        if (count > films.size()) {
            count = films.size();
        }
        return new ArrayList<>(films.subList(0, count));
    }

}
