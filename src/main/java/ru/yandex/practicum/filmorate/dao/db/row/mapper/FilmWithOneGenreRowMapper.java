package ru.yandex.practicum.filmorate.dao.db.row.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MapperException;
import ru.yandex.practicum.filmorate.model.film.FilmWithOneGenre;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmWithOneGenreRowMapper implements RowMapper<FilmWithOneGenre> {
    @Override
    public FilmWithOneGenre mapRow(ResultSet rs, int rowNum) {
        try {
            FilmWithOneGenre film = new FilmWithOneGenre();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("realise_date").toLocalDate());
            film.setLikes(rs.getInt("count_likes"));
            film.setDuration(rs.getInt("duration"));

            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString("rating_name"));
            film.setMpa(mpa);

            if (rs.getInt("genre_id") > 0) {
                Genre genre = new Genre();
                genre.setId(rs.getInt("genre_id"));
                genre.setTitle(rs.getString("genre_name"));
                film.setGenre(genre);
            }
            return film;
        } catch (SQLException e) {
            throw new MapperException("Could not map film row " + rowNum, e);
        }
    }
}
