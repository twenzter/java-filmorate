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
public class NewFilmRequest {
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
    private NewMPARequest mpa;
    private Set<NewGenreRequest> genres;
    private Set<Long> likes;
}
