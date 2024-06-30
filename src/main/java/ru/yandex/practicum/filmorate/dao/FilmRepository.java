package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> getFilms();

    Optional<Film> get(int id);

    Film add(Film film);

    Film update(Film film);

    void likeIt(int id, int userId);

    void removeLike(int id, int userId);

    Collection<Integer> getLikes(int id);

    List<Film> getTopFilms(int count);
}
