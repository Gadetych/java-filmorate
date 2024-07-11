package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

public interface BaseDBRepository<T> {
    boolean exists(String sql, int id);

    int insert(String sql, Object... params);

    void batchUpdate(String sql, Film obj);

    Optional<T> selectOne(String sql, Object... params);

    List<T> selectMore(String sql, Object... params);

    List<Integer> selectMoreInt(String sql, Object... params);

    void update(String sql, Object... params);

    void delete(String sql, Object... params);
}
