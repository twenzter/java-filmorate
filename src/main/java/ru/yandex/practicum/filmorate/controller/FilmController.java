package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<Film>> findAll() {
        log.info("GET /films request to get the list of films");
        return ResponseEntity.ok(filmService.findAll());
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("POST /films request to create film");
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.create(film));
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        log.info("PUT /films request for update film data by id");
        return ResponseEntity.ok(filmService.update(film));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> findFilm(@PathVariable Long id) {
        log.info("GET /films/{id} request to find film by id");
        return ResponseEntity.ok(filmService.findFilm(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT /films/{id}/like/{userId} request to add like to film");
        return ResponseEntity.ok(filmService.addLikeToFilm(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE /films/{id}/like/{userId} request to delete like from film");
        return ResponseEntity.ok(filmService.deleteLikeFromFilm(id, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("GET /films/popular request to get popular films");
        if (count <= 0) {
            log.warn("Count must be positive");
            throw new ValidationException("error", "Count must be positive");
        }
        return ResponseEntity.ok(filmService.findPopularFilms(count));
    }
}