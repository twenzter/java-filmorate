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
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
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
            log.warn("Login can't contain spaces");
            throw new ValidationException("login", "Login can't contain spaces");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday can't be in future");
            throw new ValidationException("birthday", "Birthday can't be in future");
        }

        User user = UserMapper.mapToUser(newUser);

        log.trace("Add user to storage");
        user = userStorage.add(user);
        log.info("User has been created!");
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(Long id, UpdateUserRequest updateUser) {
        if (updateUser.getLogin().contains(User.SPACE_SYMBOL)) {
            log.warn("Login can't contain spaces");
            throw new ValidationException("login", "Login can't contain spaces");
        }
        if (updateUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday can't be in future");
            throw new ValidationException("birthday", "Birthday can't be in future");
        }

        log.trace("Get oldUser class by id");
        User oldUser = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));

        log.debug("Set new data for user");
        User user = UserMapper.updateUserFields(oldUser, updateUser);

        log.info("User data has been updated!");
        return UserMapper.mapToUserDto(user);
    }

    public UserDto findUser(Long id) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        return UserMapper.mapToUserDto(user);
    }

    public Set<UserDto> addFriend(Long id, Long friendId) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        User friend = userStorage.findOne(friendId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + friendId + " hasn't been found"));

        Map<Long, FriendshipStatus> userFriends = user.getFriends();
        Map<Long, FriendshipStatus> friendsFriends = friend.getFriends();
        log.debug("Add friend to user");
        if (userFriends.get(friendId) == FriendshipStatus.CONFIRMED) {
            log.warn("User with friendId is already in friends");
            throw new ValidationException("error", "User with friendId is already in friends");
        }
        if (friendsFriends.get(id) == FriendshipStatus.UNCONFIRMED) {
            userFriends.put(friendId, FriendshipStatus.CONFIRMED);
            friendsFriends.put(id, FriendshipStatus.CONFIRMED);
            user.setFriends(userFriends);
            friend.setFriends(friendsFriends);
        }
        if (!friendsFriends.containsKey(id)) {
            userFriends.put(friendId, FriendshipStatus.UNCONFIRMED);
            user.setFriends(userFriends);
        }

        log.debug("Return user set with added friend");
        return getFriendsList(id);
    }

    public Set<UserDto> deleteFriend(Long id, Long friendId) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        User friend = userStorage.findOne(friendId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + friendId + " hasn't been found"));

        Map<Long, FriendshipStatus> userFriends = user.getFriends();
        Map<Long, FriendshipStatus> friendsFriends = friend.getFriends();
        log.debug("Delete friend to user");
        if (!userFriends.containsKey(friendId)) {
            log.warn("User with friendId is not in friends");
            throw new ValidationException("error", "User with friendId is not in friends");
        }
        if (userFriends.get(friendId) == FriendshipStatus.CONFIRMED) {
            userFriends.remove(friendId);
            friendsFriends.put(id, FriendshipStatus.UNCONFIRMED);
            user.setFriends(userFriends);
            friend.setFriends(friendsFriends);
        }
        if (userFriends.get(friendId) == FriendshipStatus.UNCONFIRMED) {
            userFriends.remove(friendId);
            user.setFriends(userFriends);
        }

        log.debug("Return user set with removed friend");
        return getFriendsList(id);
    }

    public Set<UserDto> getFriendsList(Long id) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        log.debug("Return user friends list");
        return user.getFriends().keySet().stream()
                .map(userStorage::findOne)
                .filter(Optional::isPresent)
                .map(opt -> UserMapper.mapToUserDto(opt.get()))
                .collect(Collectors.toSet());
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        User otherUser = userStorage.findOne(otherId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + otherId + " hasn't been found"));

        log.debug("Return user common friends list");
        return user.getFriends().keySet().stream()
                .filter(ID -> otherUser.getFriends().containsKey(ID))
                .map(userStorage::findOne)
                .filter(Optional::isPresent)
                .map(opt -> UserMapper.mapToUserDto(opt.get()))
                .collect(Collectors.toSet());
    }
}
