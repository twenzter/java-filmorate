package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
public class UpdateFilmRequest {
    @NotNull(message = "id can't be empty")
    private Long id;
    private String name;
    @Size(max = Film.MAX_DESCRIPTION_LENGTH, message = "Film description must have less than 200 symbols")
    private String description;
    private LocalDate releaseDate;
    @Min(value = Film.MIN_FILM_DURATION, message = "Film duration must be at least 1 minute")
    private Integer duration;
    private NewMPARequest mpa;
    private Set<NewGenreRequest> genres;

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasMPA() {
        return mpa != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }
}
