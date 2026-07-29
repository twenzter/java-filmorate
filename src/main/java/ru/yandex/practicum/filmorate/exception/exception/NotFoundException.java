package ru.yandex.practicum.filmorate.exception.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String parameter;
    private final String description;

    public NotFoundException(String parameter, String description) {
        this.parameter = parameter;
        this.description = description;
    }
}
