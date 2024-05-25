package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("<== Get all films");
        return filmService.getFilms();
    }

    @PostMapping
    @Validated(Marker.Create.class)
    public Film add(@RequestBody @Valid Film film) {
        log.info("==> POST /films {}", film);
        Film newFilm = filmService.add(film);
        log.info("<== Added film: {}", newFilm);
        return newFilm;
    }

    @PutMapping
    @Validated(Marker.Update.class)
    public Film update(@RequestBody @Valid Film film) {
        log.info("==> PUT /films {}", film);
        Film newFilm = filmService.update(film);
        log.info("<== Updated film: {}", newFilm);
        return newFilm;
    }
}
