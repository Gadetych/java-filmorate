package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmWithOneGenre;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.*;

@Repository
@Qualifier("DB")
public class FilmDBRepository extends BaseDBRepositoryImpl<FilmWithOneGenre> implements FilmRepository {

    public FilmDBRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmWithOneGenre> mapper) {
        super(jdbcTemplate, mapper);
    }

    private Map<Integer, Film> filmMapping(List<FilmWithOneGenre> list) {
        Map<Integer, Film> filmMap = new HashMap<>();
        for (FilmWithOneGenre f : list) {
            Integer id = f.getId();
            Film film = filmMap.get(id);
            if (film == null) {
                film = new Film();
                film.setId(f.getId());
                film.setName(f.getName());
                film.setDuration(f.getDuration());
                film.setDescription(f.getDescription());
                film.setLikes(f.getLikes());
                film.setMpa(f.getMpa());
                film.setReleaseDate(f.getReleaseDate());
                film.setGenres(new TreeSet<>(Comparator.comparing(Genre::getId)));
            }
            Genre genre = f.getGenre();
            if (genre != null) {
                film.getGenres().add(genre);
            }
            filmMap.put(id, film);
        }
        return filmMap;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT EXISTS (SELECT id FROM films WHERE id = ?)";
        return exists(sql, id);
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT f.*,\n" +
                "       r.name rating_name,\n" +
                "       fg.genre_id,\n" +
                "       g.title genre_name\n" +
                "FROM films f\n" +
                "LEFT JOIN ratings r ON r.id = f.rating_id\n" +
                "LEFT JOIN film_genre fg ON fg.film_id = f.id\n" +
                "LEFT JOIN genres g ON g.id = fg.genre_id";
        List<FilmWithOneGenre> list = selectMore(query);
        return new ArrayList<>(filmMapping(list).values());
    }

    @Override
    public Optional<Film> get(int id) {
        String query = "SELECT f.*,\n" +
                "       r.id mpa_id,\n" +
                "       r.name rating_name,\n" +
                "       fg.genre_id,\n" +
                "       g.title genre_name\n" +
                "FROM films f\n" +
                "LEFT JOIN ratings r ON r.id = f.rating_id\n" +
                "LEFT JOIN film_genre fg ON fg.film_id = f.id\n" +
                "LEFT JOIN genres g ON g.id = fg.genre_id\n" +
                "WHERE f.id = ?;";
        List<FilmWithOneGenre> list = selectMore(query, id);
        Film film = filmMapping(list).get(id);
        return Optional.ofNullable(film);
    }

    @Override
    public Film add(Film film) {
        String createFilm = "INSERT INTO films (name, description, realise_date, duration, count_likes, rating_id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        int id = insert(createFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getMpa().getId());
        film.setId(id);
        if (film.getGenres() != null) {
            String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
            batchUpdate(query, film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String queryUpdateFilm = "UPDATE films SET name = ?, description = ?, realise_date = ?, duration = ?, count_likes = ?, rating_id = ? WHERE id = ?";
        update(queryUpdateFilm, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getMpa().getId(), film.getId());

        if (film.getGenres() != null) {
            String queryDeleteFilmGenres = "DELETE FROM film_genre WHERE film_id = ?;";
            delete(queryDeleteFilmGenres, film.getId());

            String queryUpdateFilmGenre = "UPDATE film_genre SET film_id = ?, genre_id = ?;";
            batchUpdate(queryUpdateFilmGenre, film);
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
        return selectMoreInt(query, id);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String query = "SELECT f.*,\n" +
                "       r.id mpa_id,\n" +
                "       r.name rating_name,\n" +
                "       fg.genre_id,\n" +
                "       g.title genre_name\n" +
                "FROM films f\n" +
                "LEFT JOIN ratings r ON r.id = f.rating_id\n" +
                "LEFT JOIN film_genre fg ON fg.film_id = f.id\n" +
                "LEFT JOIN genres g ON g.id = fg.genre_id\n" +
                "ORDER BY f.count_likes DESC\n" +
                "LIMIT ?;";
        List<FilmWithOneGenre> films = selectMore(query, count);
        return new ArrayList<>(filmMapping(films).values());
    }
}
