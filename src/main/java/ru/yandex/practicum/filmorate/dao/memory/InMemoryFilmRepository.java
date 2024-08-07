package ru.yandex.practicum.filmorate.dao.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;

@Repository
@Qualifier("memory")
public class InMemoryFilmRepository implements FilmRepository {
    Map<Integer, Film> films = new HashMap<>();
    Map<Integer, Set<Integer>> likes = new HashMap<>();
    private Integer maxId = 0;

    @Override
    public boolean exists(int id) {
        return films.containsKey(id);
    }

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
        film.setLikes(0);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Integer id = film.getId();
        Film oldFilm = films.get(id);
        film.setLikes(oldFilm.getLikes());
        films.put(id, film);
        return film;
    }

    @Override
    public void likeIt(int id, int userId) {
        Set<Integer> usersLikes = likes.computeIfAbsent(id, k -> new HashSet<>());
        usersLikes.add(userId);
        films.get(id).setLikes(usersLikes.size());
    }

    @Override
    public void removeLike(int id, int userId) {
        likes.get(id).remove(userId);
        films.get(id).setLikes(likes.get(id).size());
    }

    @Override
    public Collection<Integer> getLikes(int id) {
        Set<Integer> likesUserID = likes.get(id);
        if (likesUserID == null) {
            likesUserID = new HashSet<>();
        }
        likes.put(id, likesUserID);
        return likesUserID;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        count = Math.min(count, likes.size());
        List<Film> topFilms = films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikes).reversed())
                .limit(count)
                .toList();
        return topFilms;
    }

    private Integer nextId() {
        maxId++;
        return maxId;
    }
}
