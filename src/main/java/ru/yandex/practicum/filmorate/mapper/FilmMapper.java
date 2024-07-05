package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

@Component
@RequiredArgsConstructor
public class FilmMapper implements Mapper<Film, FilmDto> {
    private final MpaMapper mpaMapper;

    @Override
    public Film dtoToModel(FilmDto dto) {
        Film model = new Film();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setDuration(dto.getDuration());
        model.setLikes(dto.getLikes());
        model.setReleaseDate(dto.getReleaseDate());
        Mpa mpa = mpaMapper.dtoToModel(dto.getMpa());
        model.setMpa(mpa);
        return model;
    }

    @Override
    public FilmDto modelToDto(Film model) {
        FilmDto dto = new FilmDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setDuration(model.getDuration());
        dto.setLikes(model.getLikes());
        dto.setReleaseDate(model.getReleaseDate());
        MpaDto mpaDto = mpaMapper.modelToDto(model.getMpa());
        dto.setMpa(mpaDto);
        return dto;
    }
}
