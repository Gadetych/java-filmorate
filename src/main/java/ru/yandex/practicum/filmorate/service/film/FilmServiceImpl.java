package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("DB")
    private final FilmRepository filmRepository;
    @Qualifier("DB")
    private final UserRepository userRepository;

    private final FilmMapper filmMapper;

    @Override
    public List<FilmDto> getFilms() {
        List<Film> films = filmRepository.getFilms();
        List<FilmDto> filmDtoList = null;
        if (films != null) {
            filmDtoList = films.stream()
                    .map(filmMapper::modelToDto)
                    .toList();
        }
        return filmDtoList;
    }

    @Override
    public FilmDto get(int id) {
        Film model = filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        return filmMapper.modelToDto(model);
    }

    @Override
    public FilmDto add(FilmDto filmDto) {
        Film film = filmMapper.dtoToModel(filmDto);
        film = filmRepository.add(film);
        filmDto = filmMapper.modelToDto(film);
        return filmDto;
    }

    @Override
    public FilmDto update(FilmDto dto) {
        int id = dto.getId();
        if (!filmRepository.exists(id)) {
            throw new NotFoundException("The movie with the ID was not found: " + id);
        }
        Film model = filmMapper.dtoToModel(dto);
        model = filmRepository.update(model);
        dto = filmMapper.modelToDto(model);
        return dto;
    }

    @Override
    public void likeIt(int id, int userId) {
        if (!filmRepository.exists(id)) {
            throw new NotFoundException("The movie with the ID was not found: " + id);
        }
        if (!userRepository.exists(userId)) {
            throw new NotFoundException("The movie with the ID was not found: " + userId);
        }
        filmRepository.likeIt(id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        filmRepository.get(id).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepository.get(userId).orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
        if (filmRepository.getLikes(id) == null || filmRepository.getLikes(id).isEmpty()) {
            return;
        }
        filmRepository.removeLike(id, userId);
    }

    @Override
    public List<FilmDto> getLikes(int id) {
        if (!filmRepository.exists(id)) {
            throw new NotFoundException("The movie with the ID was not found: " + id);
        }
        Collection<Integer> likes = filmRepository.getLikes(id);
        List<FilmDto> result = new ArrayList<>();
        likes.forEach(f -> result.add(get(f)));
        return result;
    }

    @Override
    public List<FilmDto> getTopFilms(int count) {
        List<Film> modelList = filmRepository.getTopFilms(count);
        return modelList.stream()
                .map(filmMapper::modelToDto)
                .sorted(Comparator.comparingInt(FilmDto::getLikes).reversed())
                .toList();
    }
}
