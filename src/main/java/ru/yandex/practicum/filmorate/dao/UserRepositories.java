package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepositories {

    List<User> getUsers();

    Optional<User> get(int id);

    User add(User user);

    User update(User user);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    Collection<Integer> getFriends(int id);

    List<Integer> getCommonFriends(int id, int otherId);
}
