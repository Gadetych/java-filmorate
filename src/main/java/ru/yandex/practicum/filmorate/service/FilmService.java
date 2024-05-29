package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.Film;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film get(int id);

    Film add(Film film);

    Film update(Film film);

    void likeIt(int id, int userId);

    void removeLike(int id, int userId);

    List<Film> getLikes(int id);

    List<Film> getTopFilms(int count);
}
