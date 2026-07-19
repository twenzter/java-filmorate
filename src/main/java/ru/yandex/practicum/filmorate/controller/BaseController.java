package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public abstract class BaseController {
    protected final long generateId(Collection<Long> collection) {
        log.debug("Calculating next id");
        long currentId = collection.stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("Return next id");
        return ++currentId;
    }
}
