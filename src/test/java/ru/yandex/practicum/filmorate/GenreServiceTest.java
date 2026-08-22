package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.stream.Stream;

public class GenreServiceTest {
    public GenreService genreService = new GenreService();

    @Test
    public void findALlGenres() {
        List<GenreDto> MPAs =
                Stream.of(Genre.COMEDY, Genre.DRAMA, Genre.CARTOON, Genre.THRILLER, Genre.DOCUMENTARY, Genre.ACTION)
                        .map(GenreMapper::mapToGenreDto)
                        .toList();
        Assertions.assertEquals(MPAs, genreService.findAll());
    }

    @Test
    public void findOneGenreById() {
        GenreDto genreDto = GenreMapper.mapToGenreDto(Genre.DOCUMENTARY);
        Assertions.assertEquals(genreDto, genreService.findGenre(5L));
    }
}
