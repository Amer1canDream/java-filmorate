package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}
