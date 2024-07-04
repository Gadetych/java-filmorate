package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

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
        List<UserDto> usersDto = userRepository.getUsers().stream()
                .map(user -> userMapper.modelToDto(user))
                .toList();
        return usersDto;
    }

    @Override
    public UserDto get(int id) {
        User user = userRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found " + id));
        UserDto userDto = userMapper.modelToDto(user);
        return userDto;
    }

    private boolean checkUserExists(Integer id) {
        if (id == null) {
            return false;
        }
        return userRepository.get(id)
                .isPresent();
    }

    @Override
    public UserDto add(UserDto userDto) {
        if (userDto.getName() == null) {
            userDto.setName(userDto.getLogin());
        }
        User user = userMapper.dtoToModel(userDto);
        user = userRepository.add(user);
        UserDto userDtoWithId = userMapper.modelToDto(user);
        return userDtoWithId;
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (!checkUserExists(userDto.getId())) {
            throw new NotFoundException("Not found user with id = " + userDto.getId());
        }
        User user = userMapper.dtoToModel(userDto);
        user = userRepository.update(user);
        UserDto newUserDto = userMapper.modelToDto(user);
        return newUserDto;
    }

    @Override
    public void remove(int id) {
        if (!checkUserExists(id)) {
            throw new NotFoundException("Not found user with id = " + id);
        }
        userRepository.remove(id);
    }

    //    Может здесь нужен другой репозиторий?
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
    public List<UserDto> getFriends(int id) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        Collection<Integer> friends = userRepository.getFriends(id);
        List<UserDto> result = new ArrayList<>();
        friends.forEach(f -> result.add(userMapper.modelToDto(userRepository.get(f).get())));
        return result;
    }

    @Override
    public List<UserDto> getCommonFriends(int id, int otherId) {
        userRepository.get(id).orElseThrow(() -> new NotFoundException("Not found user with id = " + id));
        userRepository.get(otherId).orElseThrow(() -> new NotFoundException("Not found other user with id = " + id));
        List<UserDto> result = new ArrayList<>();
        List<Integer> commonFriendsIds = userRepository.getCommonFriends(id, otherId);
        commonFriendsIds.forEach(f -> result.add(userMapper.modelToDto(userRepository.get(f).get())));
        return result;
    }
}
