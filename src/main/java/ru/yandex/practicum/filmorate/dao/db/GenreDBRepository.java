package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDBRepository extends BaseDBRepositoryImpl<Genre>{

    public GenreDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    public List<Genre> getAll() {
        String query = "SELECT * FROM genres;";
        List<Genre> result = selectMore(query);
        return result;
    }

    public Optional<Genre> getById(int id) {
        String query = "SELECT * FROM genres WHERE id = ?;";
        Optional<Genre> result = selectOne(query, id);
        return result;
    }
}
