package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<FilmDto>> findAll() {
        log.info("GET /films request to get the list of films");
        return ResponseEntity.ok(filmService.findAll());
    }

    @PostMapping
    public ResponseEntity<FilmDto> create(@Valid @RequestBody NewFilmRequest film) {
        log.info("POST /films request to create film");
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.create(film));
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@Valid @RequestBody UpdateFilmRequest film) {
        log.info("PUT /films request for update film data by id");
        return ResponseEntity.ok(filmService.update(film));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> findFilm(@PathVariable Long id) {
        log.info("GET /films/{id} request to find film by id");
        return ResponseEntity.ok(filmService.findFilm(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("PUT /films/{id}/like/{userId} request to add like to film");
        return ResponseEntity.ok(filmService.addLikeToFilm(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("DELETE /films/{id}/like/{userId} request to delete like from film");
        filmService.deleteLikeFromFilm(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<FilmDto>> findPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("GET /films/popular request to get popular films");
        if (count <= 0) {
            log.warn("Count must be positive");
            throw new ValidationException("error", "Count must be positive");
        }
        return ResponseEntity.ok(filmService.findPopularFilms(count));
    }
}