package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.db.GenreDBRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.IncorrectGenreException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDBRepository repository;
    private final GenreMapper mapper;

    @Override
    public List<GenreDto> getAll() {
        List<Genre> genres = repository.getAll();
        List<GenreDto> result = genres.stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .map(mapper::modelToDto)
                .toList();
        return result;
    }

    @Override
    public GenreDto getById(int id) {
        Genre model = repository.getById(id).orElseThrow(() -> new IncorrectGenreException("The genre with the ID was not found: " + id));
        GenreDto dto = mapper.modelToDto(model);
        return dto;
    }
}
