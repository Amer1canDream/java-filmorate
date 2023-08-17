package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FilmrotateApplicationTest {
    @Test
    public void createFilmTest() throws Exception {
        String dateString = "1967-03-25";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        final Film film = new Film(1,"nisi eiusmod","adipisicing",date,10);
    }

    @Test
    public void createUserTest() throws Exception {
        String birthdayString = "1967-03-25";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = formatter.parse(birthdayString);
        final User user = new User(1,"nisi eiusmod","nisi","adipisicing",birthday);
    }
}
