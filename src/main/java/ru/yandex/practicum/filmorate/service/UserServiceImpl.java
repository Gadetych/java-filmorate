package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("DB")
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public User get(int id) {
        return userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
    }

    private boolean checkUserExists(User user) {
        Integer id = user.getId();
        if (id == null) {
            return false;
        }
        return userRepository.get(id)
                .isPresent();
    }

    @Override
    public User add(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return userRepository.add(user);
    }

    @Override
    public User update(User user) {
        if (!checkUserExists(user)) {
            throw new NotFoundException("Not found user with id = " + user.getId());
        }
        return userRepository.update(user);
    }

    @Override
    public void addFriend(int id, int friendId) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepository.get(friendId).orElseThrow(() -> new NotFoundException("Not found friend with id = " + friendId));
        userRepository.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepository.get(friendId).orElseThrow(() -> new NotFoundException("Not found friend with id = " + id));
        if (userRepository.getFriends(id) == null || userRepository.getFriends(id).isEmpty()) {
            return;
        }
        userRepository.removeFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        Collection<Integer> friends = userRepository.getFriends(id);
        List<User> result = new ArrayList<>();
        friends.forEach(f -> result.add(userRepository.get(f).get()));
        return result;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepository.get(otherId).orElseThrow(() -> new NotFoundException("Not found other user with id = " + id));
        List<User> result = new ArrayList<>();
        List<Integer> commonFriendsIds = userRepository.getCommonFriends(id, otherId);
        commonFriendsIds.forEach(f -> result.add(userRepository.get(f).get()));
        return result;
    }
}
