package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.impl.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private MpaService service;

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return service.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        return service.getAllMpa();
    }
}
