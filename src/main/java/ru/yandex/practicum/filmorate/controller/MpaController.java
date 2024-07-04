package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<MpaDto> getAll() {
        List<MpaDto> mpaDtoList = mpaService.getAll();
        return mpaDtoList;
    }

    @GetMapping("/{id}")
    public MpaDto getById(@PathVariable @Positive Integer id) {
        MpaDto mpaDto = mpaService.getById(id);
        return mpaDto;
    }
}
