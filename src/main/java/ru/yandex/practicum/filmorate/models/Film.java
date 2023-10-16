package ru.yandex.practicum.filmorate.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Film {
    private int id;

    public Film() {};

    public Film(int id, String name, String description, Date releaseDate, int duration) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private final Set<Integer> likes = new HashSet<>();
    @NotBlank
    @NotNull
    private String name;
    private Rating mpa;
    private Set<Genre> genres;
    @Size(max = 200)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    @Min(1)
    private int duration;

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }

    public Set<Integer> getLikes() {
        return new HashSet<>(likes);
    }

}
