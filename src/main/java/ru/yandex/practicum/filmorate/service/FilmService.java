package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

public interface FilmService {

    List<FilmDto> getFilms();

    FilmDto get(int id);

    FilmDto add(FilmDto film);

    FilmDto update(FilmDto film);

    void likeIt(int id, int userId);

    void removeLike(int id, int userId);

    List<FilmDto> getLikes(int id);

    List<FilmDto> getTopFilms(int count);
}
