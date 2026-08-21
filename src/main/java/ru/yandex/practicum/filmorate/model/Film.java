package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895,12,28);
    public static final int MIN_FILM_DURATION = 1;

    private Long id;
    @NotBlank(message = "Film name can't be empty")
    private String name;
    @NotBlank(message = "Film description can't be empty")
    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Film description must have less than 200 symbols")
    private String description;
    @NotNull(message = "Film releaseDate can't be empty")
    private LocalDate releaseDate;
    @NotNull(message = "Film duration can't be empty")
    @Min(value = MIN_FILM_DURATION, message = "Film duration must be at least 1 minute")
    private Integer duration;
    @NotNull(message = "Film age limit can't be empty")
    private MPA mpa;
    private Set<Genre> genres;
    private Set<Long> likes = new HashSet<>();
}