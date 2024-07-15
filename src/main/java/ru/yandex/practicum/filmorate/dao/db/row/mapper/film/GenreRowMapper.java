package ru.yandex.practicum.filmorate.dao.db.row.mapper.film;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MapperException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) {
        try {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setTitle(rs.getString("title"));
            return genre;
        } catch (SQLException e) {
            throw new MapperException("Could not map Genre row" + rowNum, e);
        }
    }
}
