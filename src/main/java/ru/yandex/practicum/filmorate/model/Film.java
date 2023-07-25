package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    private int duration;


}
