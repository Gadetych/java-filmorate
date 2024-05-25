package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.Film;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film add(Film film);

    Film update(Film film);
}
