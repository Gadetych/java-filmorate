package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.db.MpaDBRepository;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDBRepository repository;
    private final MpaMapper mapper;

    @Override
    public List<MpaDto> getAll() {
        List<Mpa> mpaList = repository.getAll();
        List<MpaDto> mpaDtoList = mpaList.stream()
                .sorted(Comparator.comparingInt(Mpa::getId))
                .map(mapper::modelToDto)
                .toList();
        return mpaDtoList;
    }

    @Override
    public MpaDto getById(Integer id) {
        Mpa mpa = repository.getById(id).orElseThrow(() -> new NotFoundException("The MPA with the ID was not found: " + id));
        MpaDto mpaDto = mapper.modelToDto(mpa);
        return mpaDto;
    }
}
