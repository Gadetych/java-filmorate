package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("DB")
    private final FilmRepository filmRepository;
    @Qualifier("DB")
    private final UserRepository userRepository;

    private final MpaService mpaService;
    private final GenreService genreService;

    private final FilmMapper filmMapper;

    private MpaDto getMpaDto(FilmDto dto) {
        int mpaId = dto.getMpa().getId();
        MpaDto mpaDto = mpaService.getById(mpaId);
        return mpaDto;
    }

    private Set<GenreDto> getSetGenreDto(FilmDto dto) {
        Set<GenreDto> genreDtoSet = dto.getGenres();
        if (genreDtoSet == null) {
            return genreDtoSet;
        }
        genreDtoSet = genreDtoSet.stream()
                .map(genreDto -> {
                    int genreId = genreDto.getId();
                    return genreService.getById(genreId);
                })
                .sorted(Comparator.comparingInt(GenreDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return genreDtoSet;
    }

    @Override
    public List<FilmDto> getFilms() {
        List<Film> films = filmRepository.getFilms();
        List<FilmDto> filmDtoList = null;
        if (films != null) {
            filmDtoList = films.stream()
                    .map(filmMapper::modelToDto)
                    .peek(dto -> dto.setMpa(getMpaDto(dto)))
                    .peek(dto -> dto.setGenres(getSetGenreDto(dto)))
                    .toList();
        }
        return filmDtoList;
    }

    @Override
    public FilmDto get(int id) {
        Film model = filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        FilmDto dto = filmMapper.modelToDto(model);
        dto.setMpa(getMpaDto(dto));
        dto.setGenres(getSetGenreDto(dto));
        return dto;
    }

    @Override
    public FilmDto add(FilmDto filmDto) {
        Film film = filmMapper.dtoToModel(filmDto);
        film = filmRepository.add(film);
        filmDto = filmMapper.modelToDto(film);

        filmDto.setMpa(getMpaDto(filmDto));
        filmDto.setGenres(getSetGenreDto(filmDto));
        return filmDto;
    }

    @Override
    public FilmDto update(FilmDto dto) {
        int id = dto.getId();
        filmRepository.get(id).orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        Film model = filmMapper.dtoToModel(dto);
        model = filmRepository.update(model);
        dto = filmMapper.modelToDto(model);
        return dto;
    }

    @Override
    public void likeIt(int id, int userId) {
        filmRepository.get(id)
                .orElseThrow(() -> new NotFoundException("The movie with the ID was not found: " + id));
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("The user with the ID was not found: " + userId));
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
        filmRepository.get(id).orElseThrow(() -> new NotFoundException("Not found film with id = " + id));
        Collection<Integer> likes = filmRepository.getLikes(id);
        List<FilmDto> result = new ArrayList<>();
        likes.forEach(f -> result.add(get(f)));
        return result;
    }

    @Override
    public List<FilmDto> getTopFilms(int count) {
        List<Film> modelList = filmRepository.getTopFilms(count);
        List<FilmDto> dtoList = modelList.stream()
                .map(filmMapper::modelToDto)
                .toList();
        return dtoList;
    }
}
