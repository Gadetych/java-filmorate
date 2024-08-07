package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.Marker;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @AssertTrue(message = "The birthday cannot be in the future")
    public boolean isValidBirthday() {
        return birthday.isBefore(LocalDate.now());
    }
}
