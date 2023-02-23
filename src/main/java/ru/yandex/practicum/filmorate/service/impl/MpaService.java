package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.impl.MpaRepositoryImpl;
import ru.yandex.practicum.filmorate.service.MpaServiceInterface;

import java.util.List;

public class MpaService implements MpaServiceInterface {
    private MpaRepositoryImpl repository;

    @Override
    public Mpa getMpaById(int id) {
        return repository.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return repository.getAllMpa();
    }
}
