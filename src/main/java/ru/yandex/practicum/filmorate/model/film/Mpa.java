package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Mpa {
    @Positive
    @NotNull
    private Integer id;
    private String name;
}
