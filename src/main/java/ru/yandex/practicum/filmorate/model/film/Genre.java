package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Genre {
    @Positive
    @NotNull
    private Integer id;
    private String title;
}
