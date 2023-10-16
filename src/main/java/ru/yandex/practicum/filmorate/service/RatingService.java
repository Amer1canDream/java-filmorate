package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.storage.DbRatingStorage;

import java.util.List;

@Service
@Slf4j
public class RatingService {

    protected final DbRatingStorage storage;

    @Autowired
    public RatingService(DbRatingStorage storage) {
        this.storage = storage;
    }

    public List<Rating> findAll() {
        return storage.findAll();
    }

    public Rating create(Rating data) {
        return storage.create(data);
    }

    public Rating update(Rating data) {
        Rating newData = storage.update(data);
        if (newData == null) {
            log.warn("Rating not found: " + data.getId());
            throw new NotFoundException("Rating not found: " + data.getId());
        }
        return newData;
    }

    public Rating findById(int id) {
        Rating data = storage.findById(id);
        if (data == null) {
            log.warn("Rating not found: " + id);
            throw new NotFoundException("Rating not found: " + id);
        }
        return data;
    }

    public void delete(int id) {
        Rating data = storage.findById(id);
        if (data == null) {
            log.warn("Rating not found: " + id);
            throw new NotFoundException("Rating not found: " + id);
        }
        storage.delete(id);
    }
}
