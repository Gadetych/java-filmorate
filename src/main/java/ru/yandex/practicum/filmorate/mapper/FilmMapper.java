package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper implements Mapper<Film, FilmDto> {
    private final MpaMapper mpaMapper;
    private final GenreMapper genreMapper;

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

        Set<GenreDto> dtoSet = dto.getGenres();
        if (dtoSet != null) {
            Set<Genre> modelSet = dtoSet.stream()
                    .map(genreMapper::dtoToModel)
                    .collect(Collectors.toSet());
            model.setGenres(modelSet);
        }
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

        Set<Genre> modelSet = model.getGenres();
        if (modelSet != null) {
            Set<GenreDto> dtoSet = modelSet.stream()
                    .map(genreMapper::modelToDto)
                    .collect(Collectors.toSet());
            dto.setGenres(dtoSet);
        }
        return dto;
    }
}
