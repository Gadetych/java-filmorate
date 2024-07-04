package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dao.db.MpaDBRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("DB")
    private final FilmRepository filmRepository;
    @Qualifier("DB")
    private final UserRepository userRepository;
    private final MpaDBRepository mpaRepository;

    @Override
    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }

    @Override
    public Film get(int id) {
        return filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
    }

    @Override
    public Film add(Film film) {
        film = filmRepository.add(film);
        int mpaId = film.getMpa().getId();
        Mpa mpa = mpaRepository.getById(mpaId).orElseThrow(() -> new NotFoundException("The mpa with the ID was not found: " + mpaId));
        film.setMpa(mpa);
        Set<Genre> genres = new HashSet<>();
//        film.setGenres();
        return film;
    }

    @Override
    public Film update(Film film) {
        filmRepository.get(film.getId()).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + film.getId()));
        return filmRepository.update(film);
    }

    @Override
    public void likeIt(int id, int userId) {
        filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
        filmRepository.likeIt(id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        filmRepository.get(id).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepository.get(userId).orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
        if (filmRepository.getLikes(id) == null || filmRepository.getLikes(id).isEmpty()) {
            return;
        }
        filmRepository.removeLike(id, userId);
    }

    @Override
    public List<Film> getLikes(int id) {
        filmRepository.get(id).orElseThrow(() -> new NotFoundException("Not found film with id = " + id));
        Collection<Integer> likes = filmRepository.getLikes(id);
        List<Film> result = new ArrayList<>();
        likes.forEach(f -> result.add(filmRepository.get(f).get()));
        return result;
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmRepository.getTopFilms(count);
    }
}
