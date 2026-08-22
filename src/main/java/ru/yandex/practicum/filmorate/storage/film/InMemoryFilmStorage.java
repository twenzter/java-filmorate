//package ru.yandex.practicum.filmorate.storage.film;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//
//import java.util.*;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class InMemoryFilmStorage implements FilmStorage {
//    private final UserStorage userStorage;
//    private final Map<Long, Film> films = new HashMap<>();
//
//    public Film add(Film film) {
//        log.debug("Set id for film");
//        film.setId(generateId());
//        films.put(film.getId(),film);
//        return film;
//    }
//
//    public Film update(Film film) {
//        films.put(film.getId(),film);
//        return film;
//    }
//
//    public boolean delete(Long id) {
//        films.remove(id);
//        return ! films.containsKey(id);
//    }
//
//    public Optional<Film> findOne(Long id) {
//        return Optional.ofNullable(films.get(id));
//    }
//
//    public Collection<Film> findAll() {
//        return films.values();
//    }
//
//    public Film addLike(Long id, Long userId) {
//        Film film = findOne(id).orElseThrow(() -> new
//                NotFoundException("id", "Film with id " + id + " hasn't been found"));
//
//        if (film.getLikes().contains(userId)) {
//            log.warn("User already liked this film");
//            throw new ValidationException("likes", "User already liked this film");
//        }
//        log.debug("Add likes to film");
//        film.getLikes().add(userId);
//        return film;
//    }
//
//    public boolean deleteLike(Long id, Long userId) {
//        Film film = findOne(id).orElseThrow(() -> new
//                NotFoundException("id", "Film with id " + id + " hasn't been found"));
//
//        if (!film.getLikes().contains(userId)) {
//            log.warn("User hasn't liked this film");
//            throw new NotFoundException("likes", "User hasn't liked this film");
//        }
//        log.debug("Remove likes to film");
//        film.getLikes().remove(userId);
//        return true;
//    }
//
//    private long generateId() {
//        log.debug("Calculating next id");
//        long currentId = films.keySet().stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        log.trace("Return next id");
//        return ++currentId;
//    }
//
//}
