package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepositories;
import ru.yandex.practicum.filmorate.dao.UserRepositories;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepositories filmRepositories;
    @Qualifier("memory")
    private final UserRepositories userRepositories;

    @Override
    public List<Film> getFilms() {
        return filmRepositories.getFilms();
    }

    @Override
    public Film get(int id) {
        return filmRepositories.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
    }

    @Override
    public Film add(Film film) {
        return filmRepositories.add(film);
    }

    @Override
    public Film update(Film film) {
        filmRepositories.get(film.getId()).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + film.getId()));
        return filmRepositories.update(film);
    }

    @Override
    public void likeIt(int id, int userId) {
        filmRepositories.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepositories.get(userId)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
        filmRepositories.likeIt(id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        filmRepositories.get(id).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepositories.get(userId).orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
        if (filmRepositories.getLikes(id) == null || filmRepositories.getLikes(id).isEmpty()) {
            return;
        }
        filmRepositories.removeLike(id, userId);
    }

    @Override
    public List<Film> getLikes(int id) {
        filmRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found film with id = " + id));
        Collection<Integer> likes = filmRepositories.getLikes(id);
        List<Film> result = new ArrayList<>();
        likes.forEach(f -> result.add(filmRepositories.get(f).get()));
        return result;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmRepositories.getTopFilms(count);
    }
}
