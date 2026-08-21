package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Genre {
    COMEDY(1L, "Комедия"),
    DRAMA(2L, "Драма"),
    CARTOON(3L, "Мультфильм"),
    THRILLER(4L, "Триллер"),
    DOCUMENTARY(5L, "Документальный"),
    ACTION(6L, "Боевик");

    private final Long id;
    private final String name;

    public static Optional<Genre> fromId(Long id) {
        for (Genre genre: values()) {
            if (genre.getId().equals(id)) {
                return Optional.of(genre);
            }
        }
        return Optional.empty();
    }
}