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
        if (film.getGenres() != null) {
            String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
            for (Genre genre : film.getGenres()) {
                insert(query, id, genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String queryUpdateFilm = "UPDATE films SET name = ?, description = ?, realise_date = ?, duration = ?, count_likes = ?, rating_id = ? WHERE id = ?";
        update(queryUpdateFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getMpa().getId(), film.getId());

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            String queryDeleteFilmGenres = "DELETE FROM film_genre WHERE film_id = ?;";
            for (Genre genre : genres) {
                delete(queryDeleteFilmGenres, genre.getId());
            }

            String queryUpdateFilmGenre = "UPDATE film_genre SET film_id = ?, genre_id = ? WHERE genre_id = ?;";
            for (Genre genre : genres) {
                update(queryUpdateFilmGenre, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public void likeIt(int id, int userId) {
        String query = "INSERT INTO likes (film_id, user_id) VALUES (?, ?);";
        insert(query, id, userId);
        String updateLikes = "UPDATE films SET count_likes = count_likes + 1 WHERE id = ?;";
        update(updateLikes, id);
    }

    @Override
    public void removeLike(int id, int userId) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        delete(query, id, userId);
        String updateLikes = "UPDATE films SET count_likes = count_likes - 1 WHERE id = ?;";
        update(updateLikes, id);
    }

    @Override
    public Collection<Integer> getLikes(int id) {
        String query = "SELECT user_id FROM likes WHERE film_id = ?;";
        List<Integer> likes = selectMoreInt(query, id);
        return likes;
    }

    @Override
    public List<Film> getTopFilms(int count) {
//        String query = "SELECT *\n" +
//                "FROM films\n" +
//                "WHERE id IN (\n" +
//                "    SELECT film_id\n" +
//                "    FROM likes\n" +
//                "    GROUP BY film_id\n" +
//                "    ORDER BY COUNT(user_id) DESC\n" +
//                "    LIMIT ?);";
        String query = "SELECT *\n" +
                "FROM FILMS\n" +
                "ORDER BY count_likes DESC\n" +
                "LIMIT ?;";
        List<Film> films = selectMore(query, count);
        return films;
    }
}
