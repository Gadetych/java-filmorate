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
    private final GenreDBRepository genreDBRepository;
//    private final BaseDBRepositoryImpl<Film> baseDBRepository;
//    private final FilmMapper filmMapper;

    public FilmDBRepository(JdbcTemplate jdbcTemplate, RowMapper<FilmWithOneGenre> mapper, GenreDBRepository genreDBRepository) {
        super(jdbcTemplate, mapper);
        this.genreDBRepository = genreDBRepository;
//        this.baseDBRepository = baseDBRepository;
//        this.filmMapper = filmMapper;
    }

//    private Set<Genre> getGenres(Film film) {
//        String getGenreId = "SELECT genre_id FROM film_genre WHERE film_id = ?;";
//        Integer filmId = film.getId();
//        Set<Genre> genres = new HashSet<>();
//        Set<Integer> genreIds = Set.copyOf(selectMoreInt(getGenreId, filmId));
//        for (Integer genreId : genreIds) {
//            Genre genre = new Genre();
//            genre.setId(genreId);
//            genres.add(genre);
//        }
//        genres = Set.copyOf(genreDBRepository.getAllById(film.getId()));
//        return genres;
//    }

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
                film.setGenres(new HashSet<>());
            }
            Genre genre = f.getGenre();
            film.getGenres().add(genre);
            filmMap.put(id, film);
        }
        return filmMap;
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
//        Film film = baseDBRepository.selectOne(query, id).orElseThrow(() -> new NotFoundException("Film not found with id " + id));
//        film.setGenres(getGenres(film));
//        return Optional.of(film);

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
        String query = "SELECT *\n" +
                "FROM FILMS\n" +
                "ORDER BY count_likes DESC\n" +
                "LIMIT ?;";
//        List<Film> films = baseDBRepository.selectMore(query, count);
        return new ArrayList<>();
    }
}
