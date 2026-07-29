package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public void clear() {
        log.debug("Clearing films map");
        films.clear();
    }

    public Film get(Long id) {
        return films.get(id);
    }

    public boolean containsKey(Long id) {
        return films.containsKey(id);
    }

    public void put(Long id,Film film) {
        films.put(id,film);
    }

    public Set<Long> keySet() {
        return films.keySet();
    }

    public Collection<Film> values() {
        return films.values();
    }
}
