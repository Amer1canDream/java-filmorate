package ru.yandex.practicum.filmorate.models;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class Genre {
    @Size(max = 30)
    private String name;
    private int id;
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(int id) {
        this.id = id;
        this.name = "";
    }

    public Genre() {
        this.name = "";
    }

}
