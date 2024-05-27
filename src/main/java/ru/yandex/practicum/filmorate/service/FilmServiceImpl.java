package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepositories;
import ru.yandex.practicum.filmorate.dao.UserRepositories;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepositories filmRepositories;
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
        filmRepositories.getLikes(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found likes: " + id));
        filmRepositories.removeLike(id, userId);
    }

    @Override
    public Collection<Integer> getLikes(int id) {
        return filmRepositories.getLikes(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found likes: " + id));
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        return filmRepositories.getTopFilms(count);
    }
}
