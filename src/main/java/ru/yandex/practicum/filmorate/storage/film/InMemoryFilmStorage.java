package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Film add(Film film) {
        log.debug("Set id for film");
        film.setId(generateId());
        films.put(film.getId(),film);
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(),film);
        return film;
    }

    public boolean delete(Long id) {
        films.remove(id);
        return ! films.containsKey(id);
    }

    public Optional<Film> findOne(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    private long generateId() {
        log.debug("Calculating next id");
        long currentId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("Return next id");
        return ++currentId;
    }

}
