package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepositories;
import ru.yandex.practicum.filmorate.dto.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
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
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
    }

    private boolean checkUserExists(User user) {
        Integer id = user.getId();
        if (id == null) {
            return false;
        }
        return userRepositories.get(id)
                .isPresent();
    }

    @Override
    public User add(User user) {
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
        userRepositories.get(friendId).orElseThrow(() -> new NotFoundException("Not found friend with id = " + friendId));
        userRepositories.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        userRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepositories.get(friendId).orElseThrow(() -> new NotFoundException("Not found friend with id = " + id));
        if (userRepositories.getFriends(id) == null || userRepositories.getFriends(id).isEmpty()) {
            return;
        }
        userRepositories.removeFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        userRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        Collection<Integer> friends = userRepositories.getFriends(id);
        List<User> result = new ArrayList<>();
        friends.forEach(f -> result.add(userRepositories.get(f).get()));
        return result;
    }

    @Override
    public List<Integer> getCommonFriends(int id, int otherId) {
        userRepositories.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepositories.get(otherId).orElseThrow(() -> new NotFoundException("Not found other user with id = " + id));
        return userRepositories.getCommonFriends(id, otherId);
    }
}
