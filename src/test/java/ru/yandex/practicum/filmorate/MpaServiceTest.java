package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;
import java.util.stream.Stream;

public class MpaServiceTest {
    public MPAService mpaService = new MPAService();

    @Test
    public void findALlMpa() {
        List<MPADto> MPAs = Stream.of(MPA.G, MPA.PG, MPA.PG13, MPA.R, MPA.NC17)
                .map(MPAMapper::mapToMPADto)
                .toList();
        Assertions.assertEquals(MPAs, mpaService.findAll());
    }

    @Test
    public void findOneMpaById() {
        MPADto mpaDto = MPAMapper.mapToMPADto(MPA.NC17);
        Assertions.assertEquals(mpaDto, mpaService.findMPA(5L));
    }
}
