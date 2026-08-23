package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    public Collection<GenreDto> findAll() {
        return Arrays.stream(Genre.values())
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto findGenre(Long id) {
        Genre genre = Genre.fromId(id).orElseThrow(() -> new
                NotFoundException("id", "Genre with id " + id + " hasn't been found"));
        return GenreMapper.mapToGenreDto(genre);
    }
}
