package ru.yandex.practicum.filmorate.dto;

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

public class UserValidationTest {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    User user = new User();

    @BeforeEach
    void createUser() {
        user.setEmail("test-email@test.com");
        user.setLogin("Login 1");
        user.setName("Name 1");
        user.setBirthday(LocalDate.of(1992, Month.JANUARY, 1));
    }

    @Test
    void shouldValidateUserCreate() {
        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Create.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateUserUpdate() {
        user.setId(1);
        Set<ConstraintViolation<User>> violations = validator.validate(user, Marker.Update.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldInvalidUserEmail() {
        user.setEmail("@fafa@.aadq");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldLoginIsBlank() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertFalse(violations2.isEmpty());
    }

    @Test
    void shouldInvalidUserBirthday() {
        user.setBirthday(LocalDate.now().plusYears(1L));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }
}
