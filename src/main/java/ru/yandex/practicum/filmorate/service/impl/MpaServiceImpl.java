package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.impl.MpaRepositoryImpl;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepositoryImpl repository;

    @Override
    public Mpa getMpaById(int id) {
        return repository.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return repository.getAllMpa();
    }
}
