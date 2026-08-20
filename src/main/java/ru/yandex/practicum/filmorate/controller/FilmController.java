package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> findAll() {
        log.info("GET /films request to get the list of films");
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        log.info("POST /films request to create film");
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest film) {
        log.info("PUT /films request for update film data by id");
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto findFilm(@PathVariable Long id) {
        log.info("GET /films/{id} request to find film by id");
        return filmService.findFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT /films/{id}/like/{userId} request to add like to film");
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public FilmDto deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE /films/{id}/like/{userId} request to delete like from film");
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("GET /films/popular request to get popular films");
        if (count <= 0) {
            log.warn("Count must be positive");
            throw new ValidationException("error", "Count must be positive");
        }
        return filmService.findPopularFilms(count);
    }
}