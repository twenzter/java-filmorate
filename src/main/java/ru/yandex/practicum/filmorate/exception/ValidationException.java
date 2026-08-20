package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String parameter;
    private final String description;

    public ValidationException(String parameter, String description) {
        this.parameter = parameter;
        this.description = description;
    }
}
