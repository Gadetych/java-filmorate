package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.film.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {GenreDBRepository.class, GenreService.class, GenreMapper.class, GenreRowMapper.class, BaseDBRepository.class})
@Sql(scripts = {"/schema.sql", "/import.sql"})
class GenreDBRepositoryTest {
    private final GenreDBRepository repository;

    @Test
    void testGetAll() {
        List<Genre> genres = repository.getAll();
        int actual = genres.size();

        assertThat(actual).isEqualTo(6);
    }

    @Test
    void testGetById() {
        Optional<Genre> actual = repository.getById(1);
        Genre expected = new Genre();
        expected.setId(1);
        expected.setTitle("Комедия");

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).isEqualTo(expected));
    }
}