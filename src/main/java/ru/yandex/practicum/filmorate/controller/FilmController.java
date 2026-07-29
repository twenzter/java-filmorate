package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET /films request to get the list of users");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST /films request to create film");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT /films request for update film data by id");
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Long id) {
        log.info("GET /films/{id} request to find film by id");
        return filmService.findFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT /films/{id}/like/{usedId} request to add like to film");
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE /films/{id}/like/{usedId} request to delete like to film");
        return filmService.deleteLikeToFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("GET /films/popular request to get popular films");
        if (count <= 0) {
            log.warn("Count must be positive");
            throw new ValidationException("error", "Count must be positive");
        }
        return filmService.findPopularFilms(count);
    }
}