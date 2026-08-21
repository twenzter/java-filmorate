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
import java.util.Set;

@Data
public class UpdateFilmRequest {
    @NotNull(message = "id can't be empty")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "Film name can't be empty")
    private String name;
    @NotBlank(message = "Film description can't be empty")
    @Size(max = Film.MAX_DESCRIPTION_LENGTH, message = "Film description must have less than 200 symbols")
    private String description;
    @NotNull(message = "Film releaseDate can't be empty")
    private LocalDate releaseDate;
    @NotNull(message = "Film duration can't be empty")
    @Min(value = Film.MIN_FILM_DURATION, message = "Film duration must be at least 1 minute")
    private Integer duration;
    @NotNull(message = "Film age limit can't be empty")
    private MPA mpa;
    private Set<Genre> genres;
    private Set<Long> likes;

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

    public boolean hasLikes() {
        return likes != null;
    }
}
