package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Genre add(Genre genre);
    Genre update(Genre genre);
    boolean delete(Long id);
    Optional<Genre> findOne(Long id);
    Collection<Genre> findAll();
}
