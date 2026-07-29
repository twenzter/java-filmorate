package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Collection<Film> findAll() {
        return filmStorage.values();
    }

    public Film create(Film film) {
        catchMissingErrors(film);

        log.debug("Set id for film");
        film.setId(generateId(filmStorage.keySet()));
        log.trace("Add film to list");
        filmStorage.put(film.getId(),film);
        log.info("Film has been created and added to the list!");
        return film;
    }

    public Film update(Film film) {
        catchMissingErrors(film);

        checkNullFilm(film.getId());
        log.trace("Get oldFilm class by id");
        Film oldFilm = filmStorage.get(film.getId());

        log.debug("Set new data for film");
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());

        log.info("Film data has been updated!");
        return oldFilm;
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

    private void catchMissingErrors(Film film) {
        if (film.getReleaseDate().isBefore(Film.FIRST_FILM_RELEASE_DATE)) {
            log.warn("Release date is too early");
            throw new ValidationException("releaseDate", "Release date can't be early than - 1895-12-28");
        }
    }

    public Film findFilm(Long id) {
        checkNullFilm(id);
        return filmStorage.get(id);
    }

    public Film addLikeToFilm(Long id, Long userId) {
        checkNullFilm(id);
        Film film = filmStorage.get(id);
        userService.checkNullUser(userId);

        if (film.getLikes().contains(userId)) {
            log.warn("User already liked this film");
            throw new ValidationException("likes", "User already liked this film");
        }
        log.debug("Add likes to film");
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLikeToFilm(Long id, Long userId) {
        checkNullFilm(id);
        Film film = filmStorage.get(id);

        if (!film.getLikes().contains(userId)) {
            log.warn("User hasn't liked this film");
            throw new NotFoundException("likes", "User hasn't liked this film");
        }
        log.debug("Remove likes to film");
        film.getLikes().remove(userId);
        return film;
    }

    public Collection<Film> findPopularFilms(Long count) {
        Collection<Film> films = filmStorage.values();
        log.debug("Find popular films by likes");
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size());
        return films.stream().sorted(comparator.reversed()).limit(count).toList();
    }

    public void checkNullFilm(Long id) {
        if (!filmStorage.containsKey(id)) {
            log.warn("Error in film id. Film with id {} hasn't been found", id);
            throw new NotFoundException("id", "Film with id " + id + " hasn't been found");
        }
    }
}
