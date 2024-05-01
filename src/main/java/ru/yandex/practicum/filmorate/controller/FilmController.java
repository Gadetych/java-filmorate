package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> filmMap = new HashMap<>();
    LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);
    Integer maxId;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        Integer id = nextId();
        film.setId(id);
        filmMap.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateForUpdates(film);
        Integer id = film.getId();
        filmMap.put(id, film);
        return film;
    }

    private Integer nextId() {
        return maxId + 1;
    }

    private void validateForUpdates(Film film){
        Integer id = film.getId();
        if (id == null) {
            throw new ValidationException("Film id is null");
        }
        boolean flag = false;
        for (Integer i : filmMap.keySet()) {
            if (i.equals(id)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new NotFoundException("Film id does not match");
        }
        validate(film);
    }

    private void validate(Film film) {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        if (name.isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (description.length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (releaseDate.isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (duration < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
