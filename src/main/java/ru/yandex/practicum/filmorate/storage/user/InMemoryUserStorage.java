package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Map<Long, FriendshipStatus> addFriend(Long id, Long friendId) {
        Map<Long, FriendshipStatus> userFriends = findFriends(id);
        Map<Long, FriendshipStatus> friendsFriends = findFriends(friendId);

        log.debug("Add friend to user");
        if (friendsFriends.get(id) == FriendshipStatus.UNCONFIRMED) {
            userFriends.put(friendId, FriendshipStatus.CONFIRMED);
            friendsFriends.put(id, FriendshipStatus.CONFIRMED);
        } else {
            userFriends.put(friendId, FriendshipStatus.UNCONFIRMED);
        }

        log.debug("Return user set with added friend");
        return userFriends;
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        Map<Long, FriendshipStatus> userFriends = findFriends(id);
        Map<Long, FriendshipStatus> friendsFriends = findFriends(friendId);
        log.debug("Delete friend to user");

        if (userFriends.get(friendId) == FriendshipStatus.CONFIRMED) {
            friendsFriends.put(id, FriendshipStatus.UNCONFIRMED);
        }
        userFriends.remove(friendId);
    }

    @Override
    public Map<Long, FriendshipStatus> findFriends(Long id) {
        User user = findOne(id).orElseThrow(() -> new
                NotFoundException("id", "User with id " + id + " hasn't been found"));
        return user.getFriends();
    }

    private long generateId() {
        log.debug("Calculating next id");
        long currentId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("Return next id");
        return ++currentId;
    }

    public void clear() {
        users.clear();
    }

}
