package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Map<Long, User> users = new HashMap<>();

    public void clear() {
        log.debug("Clearing users map");
        users.clear();
    }

    public User get(Long id) {
        return users.get(id);
    }

    public boolean containsKey(Long id) {
        return users.containsKey(id);
    }

    public void put(Long id, User user) {
        users.put(id, user);
    }

    public Set<Long> keySet() {
        return users.keySet();
    }

    public Collection<User> values() {
        return users.values();
    }
}
