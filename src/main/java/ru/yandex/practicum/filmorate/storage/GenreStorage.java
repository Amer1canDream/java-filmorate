package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Set<Genre> getGenresByFilm(Film film);

    List<Genre> findAll();

    Genre create(Genre data);

    Genre update(Genre data);

    Genre findById(int id);

    void delete(int id);
}
