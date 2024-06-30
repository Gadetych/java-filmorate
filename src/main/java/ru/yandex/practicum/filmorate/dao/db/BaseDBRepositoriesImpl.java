package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.dao.BaseDBRepositories;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseDBRepositoriesImpl<T> implements BaseDBRepositories<T> {
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
            throw new InternalServerException("Failed to save user");
        }

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InternalServerException("Failed to get the id from user");
        }

        return key.intValue();
    }

    @Override
    public Optional<T> selectOne(String sql, Object... params) {
        T result = jdbcTemplate.queryForObject(sql, params, mapper);
        return Optional.ofNullable(result);
    }

    @Override
    public int selectMore(String sql, Object... params) {
        return 0;
    }

    @Override
    public int update(String sql, Object... params) {

        return 0;
    }

    @Override
    public int delete(String sql, Object... params) {
        return 0;
    }
}
