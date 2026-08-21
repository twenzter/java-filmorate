package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(NewFilmRequest newFilm) {
        Film film = new Film();
        film.setName(newFilm.getName());
        film.setDescription(newFilm.getDescription());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setDuration(newFilm.getDuration());
//        film.setMpa();
//        film.setGenres(newFilm.getGenres().stream()
//                .map(GenreMapper::mapToGenre)
//                .collect(Collectors.toSet()));
//        film.setLikes(newFilm.getLikes());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
//        filmDto.setMpa(film.getMpa());
//        film.setGenres(film.getGenres());
//        filmDto.setLikes(film.getLikes());
        return filmDto;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest updatedFilm) {
        if (updatedFilm.hasName()) {
            film.setName(updatedFilm.getName());
        }
        if (updatedFilm.hasDescription()) {
            film.setDescription(updatedFilm.getDescription());
        }
        if (updatedFilm.hasReleaseDate()) {
            film.setReleaseDate(updatedFilm.getReleaseDate());
        }
        if (updatedFilm.hasDuration()) {
            film.setDuration(updatedFilm.getDuration());
        }
//        if (updatedFilm.hasMPA()) {
//            film.setMpa(updatedFilm.getMpa());
//        }
//        if (updatedFilm.hasGenres()) {
//            film.setGenres(updatedFilm.getGenres());
//        }
//        if (updatedFilm.hasLikes()) {
//            film.setLikes(updatedFilm.getLikes());
//        }
        return film;
    }
}
