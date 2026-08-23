package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto create(NewUserRequest newUser) {
        if (newUser.getLogin().contains(User.SPACE_SYMBOL)) {
            log.warn("Login in create can't contain spaces");
            throw new ValidationException("login", "Login can't contain spaces");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday in create can't be in future");
            throw new ValidationException("birthday", "Birthday can't be in future");
        }

        User user = UserMapper.mapToUser(newUser);

        log.trace("Add user to storage");
        user = userStorage.add(user);
        log.info("User has been created!");
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest updateUser) {
        if (updateUser.getLogin().contains(User.SPACE_SYMBOL)) {
            log.warn("Login in update can't contain spaces");
            throw new ValidationException("login", "Login can't contain spaces");
        }
        if (updateUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday in update can't be in future");
            throw new ValidationException("birthday", "Birthday can't be in future");
        }

        log.trace("Get oldUser class by id");
        User oldUser = userStorage.findOne(updateUser.getId()).orElseThrow(() -> new
                NotFoundException("id", "User with id " + updateUser.getId() + " hasn't been found"));

        log.debug("Set new data for user");
        User user = UserMapper.updateUserFields(oldUser, updateUser);

        userStorage.update(user);
        log.info("User data has been updated!");
        return UserMapper.mapToUserDto(user);
    }

    public UserDto findUser(Long id) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        return UserMapper.mapToUserDto(user);
    }

    public Set<UserDto> addFriend(Long id, Long friendId) {
        userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        userStorage.findOne(friendId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + friendId + " hasn't been found"));

        if (userStorage.findFriends(id).get(friendId) != null) {
            log.warn("User with friendId is already add to friends");
            throw new ValidationException("error", "User with friendId is already add to friends");
        }

        userStorage.addFriend(id, friendId);
        return getFriendsList(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        userStorage.findOne(friendId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + friendId + " hasn't been found"));

        userStorage.deleteFriend(id, friendId);
    }

    public Set<UserDto> getFriendsList(Long id) {
        userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));

        return userStorage.findFriends(id).keySet().stream()
                .map(userStorage::findOne)
                .flatMap(Optional::stream)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        userStorage.findOne(otherId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + otherId + " hasn't been found"));

        log.debug("Return user common friends list");
        return userStorage.findFriends(id).keySet().stream()
                .filter(ID -> userStorage.findFriends(otherId).containsKey(ID))
                .map(userStorage::findOne)
                .flatMap(Optional::stream)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toSet());
    }
}
