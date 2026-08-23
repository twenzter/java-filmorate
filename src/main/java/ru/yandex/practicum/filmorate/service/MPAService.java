package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {

    public Collection<MPADto> findAll() {
        return Arrays.stream(MPA.values())
                .map(MPAMapper::mapToMPADto)
                .toList();
    }

    public MPADto findMPA(Long id) {
        MPA mpa = MPA.fromId(id).orElseThrow(() -> new
                NotFoundException("id", "Mpa with id " + id + " hasn't been found"));
        return MPAMapper.mapToMPADto(mpa);
    }
}
