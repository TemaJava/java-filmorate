package ru.yandex.practicum.filmorate.mapper;

public interface RawMapper<F, T> {
    T mapFrom(F object);
}
