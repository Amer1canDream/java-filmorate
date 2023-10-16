package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingController {
    protected final RatingService service;

    @Autowired
    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Rating findById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping
    public List<Rating> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Rating create(@Valid @RequestBody Rating data) {
        return service.create(data);
    }

    @PutMapping
    public Rating update(@Valid @RequestBody Rating data) {
        return service.update(data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
