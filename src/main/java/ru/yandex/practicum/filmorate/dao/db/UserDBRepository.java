package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("DB")
public class UserDBRepository extends BaseDBRepositoryImpl<User> implements UserRepository {

    public UserDBRepository(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }


    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users;";
        return selectMore(sql);
    }

    @Override
    public Optional<User> get(int id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        return selectOne(sql, id);
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?);";
        int id = insert(
                sql,
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
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";
        update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM users WHERE id = ?;";
        delete(sql, id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO friendship (user_id, friend_id, status) VALUES(?, ?, ?);";
        Collection<Integer> friends = getFriends(friendId);
        FriendStatus friendStatus = FriendStatus.PENDING;
        if (friends.contains(id)) {
            friendStatus = FriendStatus.CONFIRMED;
        }

        insert(sql, id, friendId, friendStatus.toString());
    }

    @Override
    public void removeFriend(int id, int friendId) {
        String queryDelete = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?;";
        delete(queryDelete, id, friendId);
    }

    @Override
    public Collection<Integer> getFriends(int id) {
        String sql = "SELECT friend_id FROM friendship WHERE user_id = ?;";
        return selectMoreInt(sql, id);
    }

    @Override
    public List<Integer> getCommonFriends(int id, int otherId) {
        return List.of();
    }
}
