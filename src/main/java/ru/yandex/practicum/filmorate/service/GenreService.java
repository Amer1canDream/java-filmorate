package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    protected final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public void validationBeforeCreate(Genre data) {
    }

    public void validationBeforeUpdate(Genre data) {
        validateId(data.getId());
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre create(Genre data) {
        validationBeforeCreate(data);
        return genreStorage.create(data);
    }

    public Genre update(Genre data) {
        validationBeforeUpdate(data);
        Genre newData = genreStorage.update(data);
        if (newData == null) {
            log.warn("Genre not found: " + data.getId());
            throw new NotFoundException("Genre not found: " + data.getId());
        }
        return newData;
    }

    public void validateId(int id) {
        if (id == 0) {
            log.warn("Id can not be null: " + id);
            throw new InvalidIdException("Id can not be null: "  + id);
        }
        if (id < 0) {
            log.warn("Id can not be less than zero: " + id);
            throw new NotFoundException("Id can not be less than zero: " + id);
        }
    }

    public Genre findById(int id) {
        validateId(id);
        Genre data = genreStorage.findById(id);
        if (data == null) {
            log.warn("Genre not found: " + id);
            throw new NotFoundException("Genre not found: " + id);
        }
        return data;
    }

    public void delete(int id) {
        validateId(id);
        Genre data = genreStorage.findById(id);
        if (data == null) {
            log.warn("Genre not found: " + id);
            throw new NotFoundException("Genre not found: " + id);
        }
        genreStorage.delete(id);
    }
}
