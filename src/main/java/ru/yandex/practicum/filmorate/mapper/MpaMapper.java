package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.film.Mpa;

@Component
public class MpaMapper implements Mapper <Mpa, MpaDto>{
    @Override
    public Mpa dtoToModel(MpaDto dto) {
        Mpa mpa = new Mpa();
        mpa.setId(dto.getId());
        mpa.setName(dto.getName());
        return mpa;
    }

    @Override
    public MpaDto modelToDto(Mpa model) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(model.getId());
        mpaDto.setName(model.getName());
        return mpaDto;
    }
}
