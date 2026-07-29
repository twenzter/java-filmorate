package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.values();
    }

    public User create(User user) {
        catchMissingErrors(user);

        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Set user name as login");
            user.setName(user.getLogin());
        }
        log.debug("Set user id");
        user.setId(generateId(userStorage.keySet()));
        log.trace("Add user to list");
        userStorage.put(user.getId(),user);
        log.info("User has been created and added to the list!");
        return user;
    }

    public User update(User user) {
        catchMissingErrors(user);

        checkNullUser(user.getId());
        log.trace("Get oldUser class by id");
        User oldUser = userStorage.get(user.getId());

        log.debug("Set new data for user");
        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());

        if (user.getName() != null && !user.getName().isBlank()) {
            log.debug("Set new name for user");
            oldUser.setName(user.getName());
        } else {
            log.trace("Set name as login");
            oldUser.setName(user.getLogin());
        }

        log.info("User data has been updated!");
        return oldUser;
    }

    private long generateId(Collection<Long> collection) {
        log.debug("Calculating next id");
        long currentId = collection.stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("Return next id");
        return ++currentId;
    }

    private void catchMissingErrors(User user) {
        if (user.getLogin().contains(User.SPACE_SYMBOL)) {
            log.warn("Login can't contain spaces");
            throw new ValidationException("login", "Login can't contain spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday can't be in future");
            throw new ValidationException("birthday", "Birthday can't be in future");
        }
    }

    public User findUser(Long id) {
        checkNullUser(id);
        return userStorage.get(id);
    }

    public Set<User> addFriend(Long id, Long friendId) {
        checkNullUser(id);
        User user = userStorage.get(id);
        checkNullUser(friendId);
        User friend = userStorage.get(friendId);

        if (user.getFriends().contains(friend.getId())) {
            log.warn("User with friendId is already in friends");
            throw new ValidationException("error", "User with friendId is already in friends");
        }
        log.debug("Add friend to user");
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.debug("Return user set with added friend");
        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

    public Set<User> deleteFriend(Long id, Long friendId) {
        checkNullUser(id);
        User user = userStorage.get(id);
        checkNullUser(friendId);
        User friend = userStorage.get(friendId);

        log.debug("Delete friend to user");
        if (user.getFriends().contains(friend.getId())) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
        }
        log.debug("Return user set with removed friend");
        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getFriendsList(Long id) {
        checkNullUser(id);
        User user = userStorage.get(id);
        log.debug("Return user friends list");
        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        checkNullUser(id);
        User user = userStorage.get(id);
        checkNullUser(otherId);
        User otherUser = userStorage.get(otherId);

        log.debug("Return user common friends list");
        return user.getFriends().stream()
                .filter(ID -> otherUser.getFriends().contains(ID))
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

    public void checkNullUser(Long id) {
        if (!userStorage.containsKey(id)) {
            log.warn("User with id {} hasn't been found", id);
            throw new NotFoundException("id", "User with id " + id + " hasn't been found");
        }
    }
}
