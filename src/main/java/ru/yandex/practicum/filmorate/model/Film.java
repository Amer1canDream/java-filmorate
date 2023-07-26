package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Date;

@Data
public class Film {
    private int id;

    public Film(int id, String name, String description, Date releaseDate, int duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    @Min(0)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    @Min(0)
    private int duration;

}
