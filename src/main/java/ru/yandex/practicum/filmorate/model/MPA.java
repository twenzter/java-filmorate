package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MPA {
    G(1L,"G"),
    PG(2L, "PG"),
    PG13(3L, "PG-13"),
    R(4L, "R"),
    NC17(5L, "NC-17");

    private final Long id;
    private final String name;

    public static Optional<MPA> fromId(Long id) {
        for (MPA mpa: values()) {
            if (mpa.getId().equals(id)) {
                return Optional.of(mpa);
            }
        }
        return Optional.empty();
    }
}