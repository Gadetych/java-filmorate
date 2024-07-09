package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.FilmWithOneGenreRowMapper;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.film.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.film.MpaRowMapper;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.user.UserRowMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {
        FilmDBRepository.class, FilmService.class, FilmMapper.class, FilmRowMapper.class, BaseDBRepository.class, FilmWithOneGenreRowMapper.class,
        GenreDBRepository.class, GenreService.class, GenreMapper.class, GenreRowMapper.class,
        MpaDBRepository.class, MpaService.class, MpaMapper.class, MpaRowMapper.class,
        UserDBRepository.class, UserService.class, UserMapper.class, UserRowMapper.class})
@Sql(scripts = {"/schema.sql", "/import.sql"})
class FilmDBRepositoryTest {
    private final FilmDBRepository repository;

    @Test
    void getFilms() {
        List<Film> films = repository.getFilms();
        int actual = films.size();

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void get() {
        Optional<Film> actual = repository.get(1);
        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film.getId()).isEqualTo(1));
    }

    @Test
    void add() {
        Film film = new Film();
        film.setName("fgh");
        film.setDescription("fghj");
        film.setReleaseDate(LocalDate.parse("2012-02-05"));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film.setMpa(mpa);
        film = repository.add(film);

        assertThat(film.getId()).isEqualTo(4);
    }

    @Test
    void update() {
        Film film = new Film();
        film.setId(2);
        film.setName("fgh");
        film.setDescription("fghj");
        film.setReleaseDate(LocalDate.parse("2012-02-05"));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(3);
        film.setMpa(mpa);
        film.setGenres(new HashSet<>());
        film = repository.update(film);

        assertThat(film).isEqualTo(repository.get(2).get());
    }

    @Test
    void likeIt() {
        repository.likeIt(2, 2);
        List<Integer> actual = (List<Integer>) repository.getLikes(2);

        assertThat(actual).isEqualTo(List.of(2));
    }

    @Test
    void removeLike() {
        repository.removeLike(1, 1);
        List<Integer> actual = (List<Integer>) repository.getLikes(1);

        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    void getLikes() {
        List<Integer> actual = (List<Integer>) repository.getLikes(1);

        assertThat(actual).isEqualTo(List.of(1));
    }
}