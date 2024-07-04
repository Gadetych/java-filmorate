package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Marker;
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
    public List<UserDto> getUsers() {
        log.info("<== Get all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable @Positive int id) {
        log.info("==> GET /users/{id} {}", id);
        UserDto userDto = userService.get(id);
        log.info("<== GET user = {}", userDto);
        return userDto;
    }

    @PostMapping
    @Validated(Marker.Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody @Valid UserDto userDto) {
        log.info("==> POST /users {}", userDto);
        UserDto newUserDto = null;
        newUserDto = userService.add(userDto);
        log.info("<== Added user: {}", userDto);
        return newUserDto;
    }

    @PutMapping
    @Validated(Marker.Update.class)
    public UserDto update(@RequestBody @Valid UserDto userDto) {
        log.info("==> PUT /users {}", userDto);
        UserDto newUserDto = userService.update(userDto);
        log.info("<== Updated user: {}", userDto);
        return newUserDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable @Positive int id) {
        log.info("==> DELETE /users/{}", id);
        userService.remove(id);
        log.info("<== Deleted user: {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable @Positive int id, @PathVariable @Positive int friendId) {
        log.info("==> POST /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("<== Added friend: {}", id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable @Positive int id, @PathVariable @Positive int friendId) {
        log.info("==> DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("<== Removed user: {}", id);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable @Positive int id) {
        log.info("==> GET /users/{}/friends", id);
        List<UserDto> friends = userService.getFriends(id);
        log.info("<== GET /users/{}/friends = {}", id, friends);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> commonFriends(@PathVariable @Positive int id, @PathVariable @Positive int otherId) {
        log.info("==> GET /users/{}/friends/common/{}", id, otherId);
        List<UserDto> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("<== GET /users/{}/friends/common/{} = {}", id, otherId, commonFriends);
        return commonFriends;

    }
}
