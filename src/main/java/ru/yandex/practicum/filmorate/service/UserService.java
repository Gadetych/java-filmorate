package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    List<User> getUsers();

    User get(int id);

    User add(User user) throws SQLException;

    User update(User user);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}
