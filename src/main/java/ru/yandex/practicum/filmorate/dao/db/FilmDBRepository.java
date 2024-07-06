package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.*;

@Repository
@Qualifier("DB")
public class FilmDBRepository extends BaseDBRepositoryImpl<Film> implements FilmRepository {

    public FilmDBRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    private Set<Genre> getGenres(Film film) {
        String getGenreId = "SELECT genre_id FROM film_genre WHERE film_id = ?;";
        Integer filmId = film.getId();
        Set<Genre> genres = new HashSet<>();
        Set<Integer> genreIds = Set.copyOf(selectMoreInt(getGenreId, filmId));
        for (Integer genreId : genreIds) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT * FROM films;";
        List<Film> result = selectMore(query);
        for (Film film : result) {
            film.setGenres(getGenres(film));
        }

        return result;
    }

    @Override
    public Optional<Film> get(int id) {
        String query = "SELECT * FROM films WHERE id = ?;";
        Film film = selectOne(query, id).orElseThrow(() -> new NotFoundException("Film not found with id " + id));
        film.setGenres(getGenres(film));
        return Optional.of(film);
    }

    @Override
    public Film add(Film film) {
        String createFilm = "INSERT INTO films (name, description, realise_date, duration, count_likes, rating_id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        int id = insert(createFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getMpa().getId());
        film.setId(id);

        String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
        for (Genre genre : film.getGenres()) {
            insert(query, id, genre.getId());
        }
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
