package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<FilmDto> findAll() {
        return filmStorage.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto create(NewFilmRequest newFilm) {
        if (newFilm.getReleaseDate().isBefore(Film.FIRST_FILM_RELEASE_DATE)) {
            log.warn("Release date in create is too early");
            throw new ValidationException("releaseDate", "Release date can't be early than - 1895-12-28");
        }

        Film film = FilmMapper.mapToFilm(newFilm);

        log.trace("Add film to list");
        film = filmStorage.add(film);
        log.info("Film has been created and added to the list!");
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(UpdateFilmRequest updatedFilm) {
        if (updatedFilm.getReleaseDate().isBefore(Film.FIRST_FILM_RELEASE_DATE)) {
            log.warn("Release date in update is too early");
            throw new ValidationException("releaseDate", "Release date can't be early than - 1895-12-28");
        }

        Film oldFilm = filmStorage.findOne(updatedFilm.getId()).orElseThrow(() -> new
                NotFoundException("id", "Film with id " + updatedFilm.getId() + " hasn't been found"));

        log.debug("Set new data for film");
        Film film = FilmMapper.updateFilmFields(oldFilm, updatedFilm);

        filmStorage.update(film);

        log.info("Film data has been updated!");
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto findFilm(Long id) {
        Film film = filmStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "Film with id " + id + " hasn't been found"));
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto addLikeToFilm(Long id, Long userId) {
        Film newFilm = filmStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "Film with id " + id + " hasn't been found"));
        userStorage.findOne(userId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + userId + " hasn't been found"));

        Film film = filmStorage.addLike(id, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public void deleteLikeFromFilm(Long id, Long userId) {
        filmStorage.findOne(id).orElseThrow(() -> new
                NotFoundException("id", "Film with id " + id + " hasn't been found"));
        userStorage.findOne(userId).orElseThrow(() -> new
                NotFoundException("id", "User with id " + userId + " hasn't been found"));

        filmStorage.deleteLike(id, userId);
    }

    public Collection<FilmDto> findPopularFilms(Long count) {
        Collection<Film> films = filmStorage.findAll();
        log.debug("Find popular films by likes");
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size());
        return films.stream()
                .sorted(comparator.reversed())
                .limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
