package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

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
    private Map<Integer, FriendStatus> friendStatuses;

    @AssertTrue(message = "The birthday cannot be in the future")
    public boolean isValidBirthday() {
        return birthday.isBefore(LocalDate.now());
    }
}
