package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepositories;
import ru.yandex.practicum.filmorate.dto.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepositories filmRepositories;

    @Override
    public List<Film> getFilms() {
        return filmRepositories.getFilms();
    }

    @Override
    public Film add(Film film) {
        return filmRepositories.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmRepositories.update(film);
    }
}
