package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.film.Genre;

@Component
public class GenreMapper implements Mapper<Genre, GenreDto> {
    @Override
    public Genre dtoToModel(GenreDto dto) {
        Genre model = new Genre();
        model.setId(dto.getId());
        model.setTitle(dto.getName());
        return model;
    }

    @Override
    public GenreDto modelToDto(Genre model) {
        GenreDto dto = new GenreDto();
        dto.setId(model.getId());
        dto.setName(model.getTitle());
        return dto;
    }
}
