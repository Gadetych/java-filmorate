package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
@Slf4j
@Validated
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    Integer maxId = 0;

    @GetMapping
    public List<User> getUsers() {
        log.error("Get all users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @Validated(Marker.Create.class)
    public User addUser(@RequestBody @Valid User user) {
        Integer id = nextId();
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        log.info("Added user: {}", user);
        return user;
    }

    @PutMapping
    @Validated(Marker.Update.class)
    public User updateUser(@RequestBody @Valid User user) {
        Integer id = user.getId();
        User oldUser = users.get(id);
        if (oldUser == null) {
            throw new  NotFoundException("User id in not found");
        }
        users.put(user.getId(), user);
        log.info("Updated user: {}", user);
        return user;
    }

    private Integer nextId() {
        maxId++;
        log.debug("Next id: {}", maxId);
        return maxId;
    }
}
