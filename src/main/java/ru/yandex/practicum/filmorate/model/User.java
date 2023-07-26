package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class User {
    private int id;

    public User(int id, String name, String email, String login, Date birthday) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

}
