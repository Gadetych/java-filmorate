package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDBRepository extends BaseDBRepositoryImpl<Mpa> {
    public MpaDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper);
    }

    public List<Mpa> getAll () {
        String query = "SELECT * FROM ratings;";
        List<Mpa> result = selectMore(query);
        return result;
    }

    public Optional<Mpa> getById(Integer id) {
        String query = "SELECT * FROM ratings WHERE id = ?;";
        Optional<Mpa> mpa = selectOne(query, id);
        return mpa;
    }
}
