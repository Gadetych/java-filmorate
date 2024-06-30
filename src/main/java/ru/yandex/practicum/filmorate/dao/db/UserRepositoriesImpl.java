package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BaseDBRepositories;
import ru.yandex.practicum.filmorate.dao.UserRepositories;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoriesImpl implements UserRepositories {
    private final BaseDBRepositories<User> userDBRepositories;

    private static final String ADD_USER = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
    private static final String GET_USER_ID = "SELECT * FROM users WHERE id = ?";


    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public Optional<User> get(int id) {
        return userDBRepositories.selectOne(GET_USER_ID, id);
    }

    @Override
    public User add(User user) {
        int id = userDBRepositories.insert(
                ADD_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void addFriend(int id, int friendId) {

    }

    @Override
    public void removeFriend(int id, int friendId) {

    }

    @Override
    public Collection<Integer> getFriends(int id) {
        return List.of();
    }

    @Override
    public List<Integer> getCommonFriends(int id, int otherId) {
        return List.of();
    }
}
