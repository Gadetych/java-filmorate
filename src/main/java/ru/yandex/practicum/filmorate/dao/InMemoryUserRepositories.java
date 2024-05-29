package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.User;

import java.util.*;

@Repository
public class InMemoryUserRepositories implements UserRepositories {
    Map<Integer, User> users = new HashMap<>();
    Map<Integer, Set<Integer>> friends = new HashMap<>();
    Integer maxId = 0;

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
