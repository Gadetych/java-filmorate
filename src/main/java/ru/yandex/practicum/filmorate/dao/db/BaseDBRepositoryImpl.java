package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.exception.CreateException;
import ru.yandex.practicum.filmorate.exception.CreateUserException;
import ru.yandex.practicum.filmorate.exception.UpdateUserException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class BaseDBRepositoryImpl<T> implements BaseDBRepository<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> mapper;

    @Override
    public int insert(String sql, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i <= params.length; i++) {
                ps.setObject(i, params[i - 1]);
            }
            return ps;
        }, keyHolder);

        if (rowsAffected == 0) {
            throw new CreateUserException("Failed to save user");
        }

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new CreateUserException("Failed to get the id from user");
        }

        return key.intValue();
    }

    @Override
    public void batchUpdate(String sql, Film film) {
        Set<Genre> genres = film.getGenres();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) {
                try {
                    Genre genre = (Genre) genres.toArray()[i];
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                } catch (SQLException e) {
                    throw new CreateException("Failed to save fim-genre", e);
                }
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public Optional<T> selectOne(String sql, Object... params) {
        T result = jdbcTemplate.queryForObject(sql, params, mapper);
        return Optional.ofNullable(result);
    }

    @Override
    public List<T> selectMore(String sql, Object... params) {
        return jdbcTemplate.query(sql, params, mapper);
    }

    @Override
    public List<Integer> selectMoreInt(String sql, Object... params) {
        return jdbcTemplate.queryForList(sql, Integer.class, params);
    }

    @Override
    public void update(String sql, Object... params) {
        int rowsUpdated = jdbcTemplate.update(sql, params);
        if (rowsUpdated == 0) {
            throw new UpdateUserException("Failed to update user");
        }
    }

    @Override
    public void delete(String sql, Object... params) {
        jdbcTemplate.update(sql, params);
    }
}
