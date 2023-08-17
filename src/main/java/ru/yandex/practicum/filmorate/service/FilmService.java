package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private int id = 0;
    protected final FilmStorage storage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) throws ValidateException, ParseException {
        storage.create(film);
        return film;
    }

    public Film update(Film film) throws ValidateException, ParseException {
        storage.update(film);
        return film;
    }

    public Collection getFilms() {
        Collection films = storage.getFilms();
        return films;
    }

    public Film findById(int id) {
        Film film = storage.findById(id);
        if (film == null) {
            String message = ("Film not found");
            log.warn(message);
            throw new NotFoundException(message);
        }
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
        storage.saveLikes(film);
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
        storage.saveLikes(film);
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
