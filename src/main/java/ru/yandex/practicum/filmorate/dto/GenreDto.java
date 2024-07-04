package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GenreDto {
    @Positive
    @NotNull
    private Integer id;
    private String name;
}
