package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Film;

import java.util.List;

public interface FilmRepositories {

    List<Film> getFilms();

    Film add(Film film);

    Film update(Film film);
}
