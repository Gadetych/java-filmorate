package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
public class Film {
    @Null(groups = Marker.Create.class, message = "The film id must be blank")
    @NotNull(groups = Marker.Update.class, message = "The film id must not be blank")
    private Integer id;
    @NotBlank(message = "Film name is blank")
    private String name;
    @Size(max = 200)
    private String description;
    private List<String> genre;
    private FilmRating rating;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    @Positive
    private Integer likes;

    @AssertTrue(message = "Film release date is before date 28.12.1895")
    public boolean isValidReleaseDate() {
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
