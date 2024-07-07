package ru.yandex.practicum.filmorate.dao.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.BaseDBRepository;
import ru.yandex.practicum.filmorate.dao.db.row.mapper.user.UserRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {UserDBRepository.class, UserService.class, UserMapper.class, UserRowMapper.class, BaseDBRepository.class})
@Sql(scripts = {"/schema.sql", "/import.sql"})
class UserDBRepositoryTest {
    private final UserDBRepository repository;

    @Test
    void getUsers() {
        List<User> users = repository.getUsers();
        int actual = users.size();

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void get() {
        Optional<User> actual = repository.get(1);
        User expected = new User();
        expected.setId(1);
        expected.setEmail("aadd@email");
        expected.setLogin("login");
        expected.setName("name");
        expected.setBirthday(LocalDate.parse("1995-08-10"));

        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).isEqualTo(expected));
    }

    @Test
    void add() {
        User user = new User();
        user.setEmail("asd@email");
        user.setLogin("login3");
        user.setName("name3");
        user.setBirthday(LocalDate.parse("1995-08-10"));
        user = repository.add(user);

        assertThat(user.getId()).isEqualTo(4);
    }

    @Test
    void update() {
        User user = new User();
        user.setId(3);
        user.setEmail("new_email@email");
        user.setLogin("login3");
        user.setName("name3");
        user.setBirthday(LocalDate.parse("1995-08-10"));
        user = repository.update(user);

        assertThat(user.getEmail()).isEqualTo("new_email@email");
    }

    @Test
    void addFriend() {
        repository.addFriend(1, 2);
        repository.addFriend(1, 3);
        List<Integer> a = (List<Integer>) repository.getFriends(1);
        List<Integer> b = (List<Integer>) repository.getFriends(2);

        assertThat(a.size()).isEqualTo(2);
        assertThat(b.size()).isEqualTo(0);
    }

    @Test
    void getFriends() {
        repository.addFriend(1, 2);
        repository.addFriend(1, 3);
        List<Integer> actual = (List<Integer>) repository.getFriends(1);
        List<Integer> expected = List.of(2, 3);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getCommonFriends() {
        repository.addFriend(1, 2);
        repository.addFriend(1, 3);
        repository.addFriend(2, 3);
        List<Integer> actual = repository.getCommonFriends(1, 2);
        List<Integer> expected = List.of(3);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void removeFriend() {
        repository.removeFriend(2, 3);

        assertThat(repository.getFriends(2).size()).isEqualTo(0);
    }

    @Test
    void remove() {
        repository.remove(2);

        assertThat(repository.getUsers().size()).isEqualTo(2);
    }
}