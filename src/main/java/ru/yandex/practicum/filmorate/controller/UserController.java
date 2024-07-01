package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("<== Get all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable @Positive int id) {
        log.info("==> GET /users/{id} {}", id);
        User user = userService.get(id);
        log.info("<== GET user = {}", user);
        return user;
    }

    @PostMapping
    @Validated(Marker.Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@RequestBody @Valid User user) {
        log.info("==> POST /users {}", user);
        User newUser = null;
        newUser = userService.add(user);
        log.info("<== Added user: {}", user);
        return newUser;
    }

    @PutMapping
    @Validated(Marker.Update.class)
    public User update(@RequestBody @Valid User user) {
        log.info("==> PUT /users {}", user);
        User newUser = userService.update(user);
        log.info("<== Updated user: {}", user);
        return newUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable @Positive int id) {
        log.info("==> DELETE /users/{id}", id);
        userService.remove(id);
        log.info("<== Deleted user: {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable @Positive int id, @PathVariable @Positive int friendId) {
        log.info("==> POST /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable @Positive int id, @PathVariable @Positive int friendId) {
        log.info("==> DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("<== Removed user: {}", id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive int id) {
        log.info("==> GET /users/{}/friends", id);
        List<User> friends = userService.getFriends(id);
        log.info("<== GET /users/{}/friends = {}", id, friends);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable @Positive int id, @PathVariable @Positive int otherId) {
        log.info("==> GET /users/{}/friends/common/{}", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("<== GET /users/{}/friends/common/{} = {}", id, otherId, commonFriends);
        return commonFriends;

    }
}
