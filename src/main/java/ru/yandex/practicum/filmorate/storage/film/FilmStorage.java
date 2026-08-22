package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    void clear();

    Film get(Long id);

    boolean containsKey(Long id);

    void put(Long id,Film film);

    Set<Long> keySet();

    Collection<Film> values();
}
