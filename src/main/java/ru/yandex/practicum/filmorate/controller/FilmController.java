package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FilmController {
    Map<Integer, Film> filmMap = new HashMap<>();
    LocalDate firstReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);
    Integer maxId;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Get all films");
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        Integer id = nextId();
        film.setId(id);
        filmMap.put(id, film);
        log.info("Added film: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateForUpdates(film);
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

    private void validateForUpdates(Film film){
        Integer id = film.getId();
        if (id == null) {
            log.error("Film id is null");
            throw new ValidationException("Film id is null");
        }
        boolean flag = false;
        for (Integer i : filmMap.keySet()) {
            log.debug("Validate for update, id: {}", i);
            if (i.equals(id)) {
                flag = true;
                break;
            }
        }
        log.debug("flag: {}", flag);
        if (!flag) {
            log.error("Film id {} is not found", id);
            throw new NotFoundException("Film id does not match");
        }
        validate(film);
    }

    private void validate(Film film) {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        log.debug("Film name: {}", name);
        log.debug("Film description: {}", description);
        log.debug("Film release date: {}", releaseDate);
        log.debug("Film duration: {}", duration);
        if (name.isBlank()) {
            log.error("Film name is blank");
            throw new ValidationException("Название не может быть пустым");
        }
        if (description.length() > 200) {
            log.error("Film description is too long");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (releaseDate.isBefore(firstReleaseDate)) {
            log.error("Film release date {} is before first releaseDate {}", releaseDate, firstReleaseDate);
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (duration < 0) {
            log.error("Film duration is negative");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
