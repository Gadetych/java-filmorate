package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
}
