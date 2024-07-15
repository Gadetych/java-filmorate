package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.util.List;

public interface MpaService {
    List<MpaDto> getAll();

    MpaDto getById(Integer id);
}
