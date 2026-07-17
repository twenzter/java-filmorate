package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {
    private static final FilmController filmController = new FilmController();
    private static Film film;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setName("film");
        film.setDuration(123);
        film.setDescription("The film");
        film.setReleaseDate(LocalDate.of(1999,12,1));
        filmController.clear();
    }

    @Test
    public void getFilmsEmptyList() {
        System.out.println(filmController.findAll());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void postFilm() {
        Assertions.assertEquals(film,filmController.create(film));
    }

    @Test
    public void postFilmWithoutName() {
        Film film1 = new Film();
        try {
            filmController.create(film1);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Film name can't be empty",e.getMessage());
        }
    }

    @Test
    public void postFilmWithEmptyName() {
        film.setName("     ");
        try {
            filmController.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Film name can't be empty",e.getMessage());
        }
    }

    @Test
    public void postFilmWithExceededSymbolsLimit() {
        film.setDescription("1".repeat(201));
        try {
            filmController.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Max description length - 200", e.getMessage());
        }
    }

    @Test
    public void postFilmWithMaxSymbolsLimit() {
        film.setDescription("1".repeat(200));
        Assertions.assertEquals(film, filmController.create(film));
    }

    @Test
    public void postFilmWith100Symbols() {
        film.setDescription("1".repeat(100));
        Assertions.assertEquals(film, filmController.create(film));
    }


    @Test
    public void postFilmBeforeThanFirstFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1888,12,12));
        try {
            filmController.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Release date can't be early than - 1895-12-28", e.getMessage());
        }
    }

    @Test
    public void postFilmOnFirstFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895,12,28));
        Assertions.assertEquals(film, filmController.create(film));
    }

    @Test
    public void postFilmAfterFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1900,3,30));
        Assertions.assertEquals(film, filmController.create(film));
    }

    @Test
    public void postFilmWithNormalFilmDuration() {
        film.setDuration(120);
        Assertions.assertEquals(film, filmController.create(film));
    }

    @Test
    public void postFilmWithNegativeDuration() {
        film.setDuration(-1);
        try {
            filmController.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Duration must be positive", e.getMessage());
        }
    }

    @Test
    public void postFilmWith0Duration() {
        film.setDuration(0);
        try {
            filmController.create(film);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Duration must be positive", e.getMessage());
        }
    }

    @Test
    public void updateFilm() {
        filmController.create(film);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Kill Bill");
        film2.setDuration(10);
        film2.setDescription("The series");
        film2.setReleaseDate(LocalDate.of(2004,5,3));
        Assertions.assertEquals(film2,filmController.update(film2));
    }


    @Test
    public void updateFilmWithInvalidId() {
        film.setDuration(123);
        film.setDescription("The film");
        film.setReleaseDate(LocalDate.of(1999,12,1));
        filmController.create(film);

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Kill Bill");
        film2.setReleaseDate(LocalDate.of(2004,5,3));

        try {
            filmController.update(film2);
            Assertions.fail();
        } catch (ValidationException e) {
            Assertions.assertEquals("Film with this id hasn't been found", e.getMessage());
        }
    }
}