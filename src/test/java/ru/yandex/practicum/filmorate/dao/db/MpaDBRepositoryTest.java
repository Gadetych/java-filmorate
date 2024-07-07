package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.film.MpaRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {MpaDBRepository.class, MpaService.class, MpaMapper.class, MpaRowMapper.class, BaseDBRepository.class})
@Sql(scripts = {"/schema.sql", "/import.sql"})
class MpaDBRepositoryTest {
    private final MpaDBRepository repository;

    @Test
    void getAll() {
        List<Mpa> mpaList = repository.getAll();
        int actual = mpaList.size();

        assertThat(actual).isEqualTo(5);
    }

    @Test
    void getById() {
        Optional<Mpa> actual = repository.getById(1);
        Mpa expected = new Mpa();
        expected.setId(1);
        expected.setName("G");

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).isEqualTo(expected));
    }
}