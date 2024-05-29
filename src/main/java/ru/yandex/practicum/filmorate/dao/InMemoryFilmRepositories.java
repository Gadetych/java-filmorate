package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;

@Repository
public class InMemoryFilmRepositories implements FilmRepositories {
    Map<Integer, Film> films = new HashMap<>();
    Map<Integer, Set<Integer>> likes = new HashMap<>();
    private Integer maxId = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> get(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film add(Film film) {
        Integer id = nextId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Integer id = film.getId();
        Film oldFilm = films.get(id);
        if (oldFilm == null) {
            throw new NotFoundException("Film id in not found");
        }
        films.put(id, film);
        return film;
    }

    @Override
    public void likeIt(int id, int userId) {
        Set<Integer> usersLikes = likes.computeIfAbsent(id, k -> new HashSet<>());
        usersLikes.add(userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        likes.get(id).remove(userId);
    }

    @Override
    public Optional<Collection<Integer>> getLikes(int id) {
        return Optional.ofNullable(likes.get(id));
    }

    @Override
    public List<Film> getTopFilms(int count) {
        count = Math.min(count, likes.size());
        List<Film> topFilms = likes.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().size()))
                .limit(count)
                .map(Map.Entry::getKey)
                .map(k -> films.get(k))
                .toList();
        return topFilms.reversed();
    }

    private Integer nextId() {
        maxId++;
        return maxId;
    }
}
