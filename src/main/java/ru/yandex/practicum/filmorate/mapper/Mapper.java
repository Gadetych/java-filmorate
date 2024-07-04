package ru.yandex.practicum.filmorate.mapper;

public interface Mapper<M, D> {
    M dtoToModel(D dto);

    D modelToDto(M model);
}
