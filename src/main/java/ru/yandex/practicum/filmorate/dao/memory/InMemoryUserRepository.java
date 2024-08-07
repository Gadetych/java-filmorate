package ru.yandex.practicum.filmorate.dao.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

@Repository
@Qualifier("memory")
public class InMemoryUserRepository implements UserRepository {
    Map<Integer, User> users = new HashMap<>();
    Map<Integer, Set<Integer>> friends = new HashMap<>();
    Integer maxId = 0;

    @Override
    public boolean exists(int id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> get(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User add(User user) {
        Integer id = nextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void addFriend(int id, int friendId) {
        Set<Integer> uFriendsIds = friends.computeIfAbsent(id, k -> new HashSet<>());
        uFriendsIds.add(friendId);
        Set<Integer> fFriendsIds = friends.computeIfAbsent(friendId, k -> new HashSet<>());
        fFriendsIds.add(id);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        friends.get(id).remove(friendId);
        friends.get(friendId).remove(id);
    }

    @Override
    public Collection<Integer> getFriends(int id) {
        Set<Integer> uFriends = friends.get(id);
        if (uFriends == null) {
            uFriends = new HashSet<>();
        }
        friends.put(id, uFriends);
        return uFriends;
    }

    @Override
    public List<Integer> getCommonFriends(int id, int otherId) {
        Set<Integer> uFriendsIds = friends.get(id);
        Set<Integer> fFriendsIds = friends.get(otherId);
        if (friends.get(id) == null || friends.get(otherId) == null) {
            return Collections.emptyList();
        }
        Set<Integer> commonFriendsSet = new HashSet<>(uFriendsIds);
        commonFriendsSet.retainAll(fFriendsIds);
        return new ArrayList<>(commonFriendsSet);
    }

    private Integer nextId() {
        maxId++;
        return maxId;
    }
}
