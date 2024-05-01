package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    LocalDate localDateNow = LocalDate.now();
    Integer maxId;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validate(user);
        Integer id = nextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateForUpdates(user);
        users.put(user.getId(), user);
        return user;
    }

    private Integer nextId(){
        return maxId + 1;
    }

    private void validateForUpdates(User user){
        Integer id = user.getId();
        if (id == null) {
            throw new ValidationException("Id is null");
        }
        boolean flag = false;
        for (Integer i : users.keySet()) {
            if (i.equals(id)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new NotFoundException("User id does not match");
        }
        validate(user);
    }

    private void validate(User user) {
        String email = user.getEmail();
        String  login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        if (email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (login.isBlank() || !login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (name.isBlank()) {
            name = user.getLogin();
            user.setName(name);
        }
        if (birthday.isAfter(localDateNow)) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
