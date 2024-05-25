package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmRepositories implements FilmRepositories {
    Map<Integer, Film> filmMap = new HashMap<>();
    private Integer maxId = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film add(Film film) {
        Integer id = nextId();
        film.setId(id);
        filmMap.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Integer id = film.getId();
        Film oldFilm = filmMap.get(id);
        if (oldFilm == null) {
            throw new NotFoundException("Film id in not found");
        }
        filmMap.put(id, film);
        return film;
    }

    private Integer nextId() {
        maxId++;
        return maxId;
    }
}
