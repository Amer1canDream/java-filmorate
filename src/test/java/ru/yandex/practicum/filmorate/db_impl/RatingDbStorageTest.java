package ru.yandex.practicum.filmorate.db_impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.storage.DbRatingStorage;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RatingDbStorageTest {
    private final DbRatingStorage gatingStorage;
    private int countRec;

    @BeforeEach
    void setUp() {
        countRec = gatingStorage.findAll().size();
    }

    @Test
    void findById() {
        int id1 = countRec++;
        Rating expRating = getExpRating1(id1);
        gatingStorage.create(expRating);
        Rating actRating = gatingStorage.findById(expRating.getId());
        assertEquals(expRating.getId(), actRating.getId());
        assertEquals(expRating.getName(), actRating.getName());
    }

    @Test
    void findAll() {
        int id1 = countRec++;
        Rating expRating1 = getExpRating1(id1);
        gatingStorage.create(expRating1);
        int id2 = countRec++;
        Rating expRating2 = getExpRating2(id2);
        gatingStorage.create(expRating2);

        List<Rating> actRatings = gatingStorage.findAll();
        int i1 = actRatings.size() - 2;
        int i2 = actRatings.size() - 1;
        assertEquals(expRating1.getId(),  actRatings.get(i1).getId());
        assertEquals(expRating2.getId(), actRatings.get(i2).getId());
        assertEquals(countRec, actRatings.size());
    }

    @Test
    void create() {
        int id1 = countRec++;
        Rating expRating = getExpRating1((id1));
        gatingStorage.create(expRating);
        Rating actRating = gatingStorage.findById(expRating.getId());
        assertEquals(expRating.getId(),actRating.getId());
        assertEquals(expRating.getName(),actRating.getName());
    }

    @Test
    void update() {
        int id1 = countRec++;
        Rating expRating = getExpRating1(id1);
        gatingStorage.create(expRating);
        expRating.setName("action");

        gatingStorage.update(expRating);
        Rating actRating = gatingStorage.findById(expRating.getId());

        assertEquals(expRating.getId(), actRating.getId());
        assertEquals(expRating.getName(), actRating.getName());
    }

    private Rating getExpRating1(int id) {
        Rating gating = new Rating();
        gating.setId(id);
        gating.setName("Rating1");
        return gating;
    }

    private Rating getExpRating2(int id) {
        Rating gating = new Rating();
        gating.setId(id);
        gating.setName("Rating2");
        return gating;
    }
}
