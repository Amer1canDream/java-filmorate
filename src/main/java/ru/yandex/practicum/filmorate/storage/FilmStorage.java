package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.models.Film;

import java.text.ParseException;
import java.util.List;

public interface FilmStorage {
    void create(Film film) throws ValidateException, ParseException;

    void update(Film film) throws ValidateException, ParseException;

    List<Film> getFilms();

    Film findById(int id);

    void saveLikes(Film film) throws ValidateException, ParseException;
}
