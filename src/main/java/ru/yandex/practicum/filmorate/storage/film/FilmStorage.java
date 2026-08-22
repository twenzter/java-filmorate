package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film add(Film film);
    Film update(Film film);
    boolean delete(Long id);
    Optional<Film> findOne(Long id);
    Collection<Film> findAll();

    Film addLike(Long id, Long userId);
    boolean deleteLike(Long id, Long userId);
}
