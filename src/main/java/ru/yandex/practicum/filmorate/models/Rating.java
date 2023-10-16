package ru.yandex.practicum.filmorate.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
public class Rating {
    @Size(max = 10)
    private String name;
    private int id;
    public Rating(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rating(int id) {
        this.id = id;
    }

    public Rating() {
        this.name = "";
    }
}
