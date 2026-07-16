package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895,12,28);
    private static final int MIN_FILM_DURATION = 1;

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET /films request to get the list of users");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("POST /films request to create film");
        filmChecker(film);

        log.debug("Set id for film");
        film.setId(generateId(films.keySet()));
        log.trace("Add film to list");
        films.put(film.getId(),film);
        log.info("Film has been created and added to the list!");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("PUT /films request for update film data by id");

        if (!films.containsKey(film.getId())) {
            log.warn("Error in film id. Film with this id hasn't been found");
            throw new ValidationException("Film with this id hasn't been found");
        }
        log.trace("Get oldFilm class by id");
        Film oldFilm = films.get(film.getId());
        filmChecker(film);

        log.debug("Set new name for film");
        oldFilm.setName(film.getName());
        log.debug("Set new description for film");
        oldFilm.setDescription(film.getDescription());
        log.debug("Set new release date for film");
        oldFilm.setReleaseDate(film.getReleaseDate());
        log.debug("Set new duration for film");
        oldFilm.setDuration(film.getDuration());

        log.info("Film data has been updated!");
        return oldFilm;
    }

    private void filmChecker(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Error in film name. Film name can't be empty");
            throw new ValidationException("Film name can't be empty");
        }
        if (film.getDescription() == null || film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Error in film description. Description exceed symbols limit");
            throw new ValidationException("Max description length - 200");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)) {
            log.warn("Error in film release date. Release date is too early.");
            throw new ValidationException("Release date can't be early than - 1895-12-28");
        }
        if (film.getDuration() == null || film.getDuration() < MIN_FILM_DURATION) {
            log.warn("Error in film duration. Film duration must be positive digit");
            throw new ValidationException("Duration must be positive");
        }
    }

    public void clear() {
        log.debug("Clearing films map");
        films.clear();
    }
}