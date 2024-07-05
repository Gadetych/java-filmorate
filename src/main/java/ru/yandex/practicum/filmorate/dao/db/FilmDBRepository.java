package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("DB")
public class FilmDBRepository extends BaseDBRepositoryImpl<Film> implements FilmRepository {

    public FilmDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Film> getFilms() {
        return List.of();
    }

    @Override
    public Optional<Film> get(int id) {
        String query = "SELECT * FROM films WHERE id = ?;";
        return Optional.empty();
    }

    @Override
    public Film add(Film film) {
        String createFilm = "INSERT INTO films (name, description, realise_date, duration, count_likes, rating_id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        int id = insert(createFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getMpa().getId());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void likeIt(int id, int userId) {

    }

    @Override
    public void removeLike(int id, int userId) {

    }

    @Override
    public Collection<Integer> getLikes(int id) {
        return List.of();
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return List.of();
    }
}
