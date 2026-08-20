package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

public class FilmServiceTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService =  new UserService(userStorage);
    private final FilmService filmService = new FilmService(filmStorage, userService);
    private Film film;
    private User user;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setName("film");
        film.setDuration(123);
        film.setDescription("The film");
        film.setReleaseDate(LocalDate.of(1999,12,1));
        filmStorage.clear();

        user = new User();
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1999,2,1));
        userStorage.clear();
        userService.create(user);
    }

    @Test
    public void getFilmsEmptyList() {
        System.out.println(filmService.findAll());
        Assertions.assertTrue(filmService.findAll().isEmpty());
    }

    @Test
    public void postFilm() {
        Assertions.assertEquals(film,filmService.create(film));
    }


    @Test
    public void postFilmWithMaxSymbolsLimit() {
        film.setDescription("1".repeat(200));
        Assertions.assertEquals(film, filmService.create(film));
    }

    @Test
    public void postFilmWith100Symbols() {
        film.setDescription("1".repeat(100));
        Assertions.assertEquals(film, filmService.create(film));
    }


    @Test
    public void postFilmBeforeThanFirstFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1888,12,12));
        try {
            filmService.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Release date can't be early than - 1895-12-28", e.getDescription());
        }
    }

    @Test
    public void postFilmOnFirstFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895,12,28));
        Assertions.assertEquals(film, filmService.create(film));
    }

    @Test
    public void postFilmAfterFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1900,3,30));
        Assertions.assertEquals(film, filmService.create(film));
    }

    @Test
    public void postFilmWithNormalFilmDuration() {
        film.setDuration(120);
        Assertions.assertEquals(film, filmService.create(film));
    }

    @Test
    public void updateFilm() {
        filmService.create(film);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Kill Bill");
        film2.setDuration(10);
        film2.setDescription("The series");
        film2.setReleaseDate(LocalDate.of(2004,5,3));
        Assertions.assertEquals(film2,filmService.update(film2));
    }


    @Test
    public void updateFilmWithInvalidId() {
        film.setDuration(123);
        film.setDescription("The film");
        film.setReleaseDate(LocalDate.of(1999,12,1));
        filmService.create(film);

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Kill Bill");
        film2.setReleaseDate(LocalDate.of(2004,5,3));

        try {
            filmService.update(film2);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("Film with id 2 hasn't been found", e.getDescription());
        }
    }

    @Test
    public void findFilmById() {
        filmService.create(film);
        Assertions.assertEquals(filmService.findFilm(1L), film);
    }

    @Test
    public void findFilmByWithUnknownId() {
        try {
            filmService.findFilm(1L);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("Film with id 1 hasn't been found", e.getDescription());
        }

    }

    @Test
    public void addLikeToFilmByIdAndUserId() {
        filmService.create(film);

        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("film");
        film1.setDuration(123);
        film1.setDescription("The film");
        film1.setReleaseDate(LocalDate.of(1999,12,1));
        Set<Long> setLikesToFilm = new HashSet<>();
        setLikesToFilm.add(user.getId());
        film1.setLikes(setLikesToFilm);

        Assertions.assertEquals(film1, filmService.addLikeToFilm(1L,1L));
    }

    @Test
    public void addLikeToFilmByIdAndUnknownUserId() {
        filmService.create(film);
        try {
            filmService.addLikeToFilm(1L,2L);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("User with id 2 hasn't been found", e.getDescription());
        }
    }

    @Test
    public void addLikeToFilmAgainByIdAndSameUserId() {
        filmService.create(film);
        filmService.addLikeToFilm(1L,1L);
        try {
            filmService.addLikeToFilm(1L,1L);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("User already liked this film",e.getDescription());
        }

    }

    @Test
    public void deleteLikeFromFilmByIdAndUserId() {
        filmService.create(film);
        filmService.addLikeToFilm(1L,1L);
        filmService.deleteLikeFromFilm(1L,1L);
    }

    @Test
    public void deleteLikeFromFilmWithoutLikes() {
        filmService.create(film);
        try {
            filmService.deleteLikeFromFilm(1L,1L);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.assertEquals("User hasn't liked this film", e.getDescription());
        }
    }

    @Test
    public void findPopularFilmsWithCount() {
        filmService.create(film);
        filmService.create(film);
        filmService.create(film);
        filmService.addLikeToFilm(3L,1L);

        List<Film> set = new ArrayList<>();
        set.add(filmService.findFilm(3L));
        Assertions.assertEquals(filmService.findPopularFilms(1L), set);
    }

    @Test
    public void findPopularFilmsWithoutCount() {
        filmService.create(film);
        filmService.create(film);
        filmService.create(film);
        filmService.addLikeToFilm(3L,1L);

        List<Film> set = new ArrayList<>();
        set.add(filmService.findFilm(3L));
        set.add(filmService.findFilm(2L));
        set.add(filmService.findFilm(1L));
        Assertions.assertEquals(filmService.findPopularFilms(10L), set);
    }
}