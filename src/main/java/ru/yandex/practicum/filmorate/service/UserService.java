package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto get(int id);

    UserDto add(UserDto user);

    UserDto update(UserDto user);

    void remove(int id);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<UserDto> getFriends(int id);

    List<UserDto> getCommonFriends(int id, int otherId);
}
