package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public boolean delete(Long id) {
        users.remove(id);
        return ! users.containsKey(id);
    }

    @Override
    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
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

}
