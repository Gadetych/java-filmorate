package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @Null(groups = Marker.Create.class, message = "The user id must be blank")
    @NotNull(groups = Marker.Update.class, message = "The user id must not be blank")
    private Integer id;
    @Email(message = "This email is invalid")
    private String email;
    @NotBlank(message = "The user login must not be blank")
    private String login;
    private String name;
    private LocalDate birthday;

    @AssertTrue(message = "The birthday cannot be in the future")
    public boolean isValidBirthday() {
        return birthday.isBefore(LocalDate.now());
    }
}
