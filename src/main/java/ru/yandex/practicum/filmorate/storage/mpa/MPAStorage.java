package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

public interface MPAStorage {
    MPA add(MPA mpa);
    MPA update(MPA mpa);
    boolean delete(Long id);
    Optional<MPA> findOne(Long id);
    Collection<MPA> findAll();
}
