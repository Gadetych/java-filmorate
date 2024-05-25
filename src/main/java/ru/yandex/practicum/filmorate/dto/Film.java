package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Month;

@Data
public class Film {
    @Null(groups = Marker.Create.class, message = "The film id must be blank")
    @NotNull(groups = Marker.Update.class, message = "The film id must not be blank")
    private Integer id;
    @NotBlank(message = "Film name is blank")
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    @AssertTrue(message = "Film release date is before date 28.10.1895")
    public boolean isValidReleaseDate() {
        return releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 28));
    }
}
