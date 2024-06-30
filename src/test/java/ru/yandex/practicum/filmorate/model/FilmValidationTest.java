package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTest {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    Film film = new Film();

    @BeforeEach
    void createFilm() {
        film.setName("Film 1");
        film.setDescription("Description 1");
        film.setReleaseDate(LocalDate.of(2020, Month.JANUARY, 1));
        film.setDuration(90);
    }

    @Test
    void shouldValidateFilmCreate() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Create.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateFilmUpdate() {
        film.setId(1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Marker.Update.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNameIsBlank() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        film.setName(null);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertFalse(violations2.isEmpty());
    }

    @Test
    void shouldInvalidFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldInvalidFilmDuration() {
        film.setDuration(-9);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }
}
