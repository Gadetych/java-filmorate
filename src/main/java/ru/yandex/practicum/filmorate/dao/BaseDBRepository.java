package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDBRepository<T> {
     int insert(String sql, Object... params);

    Optional<T> selectOne(String sql, Object... params);

    List<T> selectMore(String sql, Object... params);

    void update(String sql, Object... params);

    int delete(String sql, Object... params);
}
