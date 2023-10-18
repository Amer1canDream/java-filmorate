package ru.yandex.practicum.filmorate.db_impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storage.DbGenreStorage;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class GenreDbStorageTest {
    private final DbGenreStorage genreStorage;
    private int countRec;   //Так как в таблице уже есть начальные данные

    @BeforeEach
    void setUp() {
        countRec = genreStorage.findAll().size();
    }

    @Test
    void findById() {
        int id1 = countRec++;
        Genre expGenre = getExpGenre1(id1);
        genreStorage.create(expGenre);
        Genre actGenre = genreStorage.findById(expGenre.getId());
        assertEquals(expGenre.getId(), actGenre.getId());
        assertEquals(expGenre.getName(), actGenre.getName());
    }

    @Test
    void findAll() {
        int id1 = countRec++;
        Genre expGenre1 = getExpGenre1(id1);
        genreStorage.create(expGenre1);
        int id2 = countRec++;
        Genre expGenre2 = getExpGenre2(id2);
        genreStorage.create(expGenre2);

        List<Genre> actGenres = genreStorage.findAll();
        int i1 = actGenres.size() - 2;
        int i2 = actGenres.size() - 1;
        Genre actGenre1 = actGenres.get(i1);
        Genre actGenre2 = actGenres.get(i2);
        assertEquals(expGenre1.getId(),  actGenre1.getId());
        assertEquals(expGenre2.getId(), actGenre2.getId());
        assertEquals(countRec, actGenres.size());
    }

    @Test
    void create() {
        int id1 = countRec++;
        Genre expGenre = getExpGenre1((id1));
        genreStorage.create(expGenre);
        Genre actGenre = genreStorage.findById(expGenre.getId());
        assertEquals(expGenre.getId(),actGenre.getId());
        assertEquals(expGenre.getName(),actGenre.getName());
    }

    @Test
    void update() {
        int id1 = countRec++;
        Genre expGenre = getExpGenre1(id1);
        genreStorage.create(expGenre);
        expGenre.setName("action");

        genreStorage.update(expGenre);
        Genre actGenre = genreStorage.findById(expGenre.getId());

        assertEquals(expGenre.getId(), actGenre.getId());
        assertEquals(expGenre.getName(), actGenre.getName());
    }

    private Genre getExpGenre1(int id) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Genre1");
        return genre;
    }

    private Genre getExpGenre2(int id) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Genre2");
        return genre;
    }
}
