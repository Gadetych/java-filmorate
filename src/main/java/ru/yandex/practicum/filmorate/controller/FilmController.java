package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    Map<Integer, Film> filmMap = new HashMap<>();
    Integer maxId;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Get all films");
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping
    @Validated(Marker.Create.class)
    public Film addFilm(@RequestBody @Valid Film film) {
        Integer id = nextId();
        film.setId(id);
        filmMap.put(id, film);
        log.info("Added film: {}", film);
        return film;
    }

    @PutMapping
    @Validated(Marker.Update.class)
    public Film updateFilm(@RequestBody @Valid Film film) {
        Integer id = film.getId();
        filmMap.put(id, film);
        log.info("Updated film: {}", film);
        return film;
    }

    private Integer nextId() {
        maxId++;
        log.debug("Next id: {}", maxId);
        return maxId;
    }
}
