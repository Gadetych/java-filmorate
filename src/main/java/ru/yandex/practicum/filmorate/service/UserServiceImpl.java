package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepositories;
import ru.yandex.practicum.filmorate.dto.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositories userRepositories;

    @Override
    public List<User> getUsers() {
        return userRepositories.getUsers();
    }

    @Override
    public User get(int id) {
        return userRepositories.get(id)
                .orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
    }

    private boolean checkUserExists(User user) {
        return userRepositories.get(user.getId())
                .isPresent();
    }

    @Override
    public User add(User user) {
        if (checkUserExists(user)) {
            throw new UserAlreadyExistException("User already exist with id = " + user.getId());
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return userRepositories.add(user);
    }

    @Override
    public User update(User user) {
        if (!checkUserExists(user)) {
            throw new NotFoundException("Not found user with id = " + user.getId());
        }
        return userRepositories.update(user);
    }

    @Override
    public void addFriend(int id, int friendId) {
        userRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepositories.get(friendId).orElseThrow(() -> new NotFoundException("Not found friend with id = " + id));
        userRepositories.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        userRepositories.removeFriend(id, friendId);
    }

    @Override
    public Collection<Integer> getFriends(int id) {
        return userRepositories.getFriends(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
    }

    @Override
    public Collection<Integer> getCommonFriends(int id, int otherId) {
        userRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepositories.get(otherId).orElseThrow(() -> new NotFoundException("Not found other user with id = " + id));
        return userRepositories.getCommonFriends(id, otherId);
    }
}
