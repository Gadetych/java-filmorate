package ru.yandex.practicum.filmorate.dao;

import java.util.Optional;

public interface BaseDBRepositories<T> {
    int insert(String sql, Object... params);

    Optional<T> selectOne(String sql, Object... params);

    int selectMore(String sql, Object... params);

    int update(String sql, Object... params);

    int delete(String sql, Object... params);
}
