package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    void clear();
    User get(Long id);
    boolean containsKey(Long id);
    void put(Long id,User user);
    Set<Long> keySet();
    Collection<User> values();
}
