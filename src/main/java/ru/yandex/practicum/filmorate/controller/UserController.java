package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    LocalDate localDateNow = LocalDate.now();
    Integer maxId;

    @GetMapping
    public List<User> getUsers() {
        log.error("Get all users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validate(user);
        Integer id = nextId();
        user.setId(id);
        users.put(id, user);
        log.info("Added user: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateForUpdates(user);
        users.put(user.getId(), user);
        log.info("Updated user: {}", user);
        return user;
    }

    private Integer nextId(){
        maxId++;
        log.debug("Next id: {}", maxId);
        return maxId;
    }

    private void validateForUpdates(User user){
        Integer id = user.getId();
        if (id == null) {
            log.error("Id is null");
            throw new ValidationException("Id is null");
        }
        boolean flag = false;
        for (Integer i : users.keySet()) {
            log.debug("Validate for update, id: {}", i);
            if (i.equals(id)) {
                flag = true;
                break;
            }
        }
        log.debug("flag: {}", flag);
        if (!flag) {
            log.error("User id {} does not match", id);
            throw new NotFoundException("User id does not match");
        }
        validate(user);
    }

    private void validate(User user) {
        String email = user.getEmail();
        String  login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        log.debug("email: {}", email);
        log.debug("login: {}", login);
        log.debug("name: {}", name);
        log.debug("birthday: {}", birthday);
        if (email.isBlank() || !email.contains("@")) {
            log.error("Invalid email address");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (login.isBlank() || !login.contains(" ")) {
            log.error("Invalid login address");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (name.isBlank()) {
            name = user.getLogin();
            user.setName(name);
            log.debug("name: {}", name);
        }
        if (birthday.isAfter(localDateNow)) {
            log.error("Birthday {} is after localDateNow {}", birthday, localDateNow);
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
