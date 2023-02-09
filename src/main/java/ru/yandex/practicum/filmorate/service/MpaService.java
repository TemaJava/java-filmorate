package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.interfaces.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService implements MpaServiceInterface {
    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    @Override
    public Mpa getById(int id) {
        if (mpaRepository.getById(id) == null) {
            throw new NotFoundException("Мпа не найден");
        }
        return mpaRepository.getById(id);
    }
}