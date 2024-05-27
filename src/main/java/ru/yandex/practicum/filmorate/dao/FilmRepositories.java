package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepositories {

    List<Film> getFilms();

    Optional<Film> get(int id);

    Film add(Film film);

    Film update(Film film);

    void likeIt(int id, int userId);

    void removeLike(int id, int userId);

    Optional<Collection<Integer>> getLikes(int id);

    List<Integer> getTopFilms(int count);
}
