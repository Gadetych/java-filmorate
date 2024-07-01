package ru.yandex.practicum.filmorate.dao.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("DB")
public class UserDBRepository extends BaseDBRepositoryImpl<User> implements UserRepository {

    private static final String ADD_USER = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
    private static final String GET_USER_ID = "SELECT * FROM users WHERE id = ?";

    public UserDBRepository(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }


    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return selectMore(sql);
    }

    @Override
    public Optional<User> get(int id) {
        return selectOne(GET_USER_ID, id);
    }

    @Override
    public User add(User user) {
        int id = insert(
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
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        delete(sql, id);
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
