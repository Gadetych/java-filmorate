package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("DB")
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(userMapper::modelToDto)
                .toList();
    }

    @Override
    public UserDto get(int id) {
        User user = userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserDto add(UserDto userDto) {
        if (userDto.getName() == null) {
            userDto.setName(userDto.getLogin());
        }
        User user = userMapper.dtoToModel(userDto);
        user = userRepository.add(user);
        return userMapper.modelToDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (!userRepository.exists(userDto.getId())) {
            throw new NotFoundException("Not found user with id = " + userDto.getId());
        }
        User user = userMapper.dtoToModel(userDto);
        user = userRepository.update(user);
        return userMapper.modelToDto(user);
    }

    @Override
    public void remove(int id) {
        if (!userRepository.exists(id)) {
            return;
        }
        userRepository.remove(id);
    }

    //    Может здесь нужен другой репозиторий?
    @Override
    public void addFriend(int id, int friendId) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException("Not found user with id = " + id);
        }
        if (!userRepository.exists(friendId)) {
            throw new NotFoundException("Not found user with id = " + friendId);
        }

        userRepository.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException("Not found user with id = " + id);
        }
        if (!userRepository.exists(friendId)) {
            throw new NotFoundException("Not found user with id = " + friendId);
        }
        if (userRepository.getFriends(id) == null || userRepository.getFriends(id).isEmpty()) {
            return;
        }
        userRepository.removeFriend(id, friendId);
    }

    @Override
    public List<UserDto> getFriends(int id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException("Not found user with id = " + id);
        }
        Collection<Integer> friends = userRepository.getFriends(id);
        List<UserDto> result = new ArrayList<>();
        friends.forEach(f -> result.add(userMapper.modelToDto(userRepository.get(f).get())));
        return result;
    }

    @Override
    public List<UserDto> getCommonFriends(int id, int otherId) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException("Not found user with id = " + id);
        }
        if (!userRepository.exists(otherId)) {
            throw new NotFoundException("Not found user with id = " + otherId);
        }
        List<UserDto> result = new ArrayList<>();
        List<Integer> commonFriendsIds = userRepository.getCommonFriends(id, otherId);
        commonFriendsIds.forEach(f -> result.add(userMapper.modelToDto(userRepository.get(f).get())));
        return result;
    }
}
