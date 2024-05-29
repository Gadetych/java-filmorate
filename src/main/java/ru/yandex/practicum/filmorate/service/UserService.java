package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User get(int id);

    User add(User user);

    User update(User user);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}
