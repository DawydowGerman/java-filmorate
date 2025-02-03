package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    boolean exists(Long id);

    List<Mpa> findAll();

    Optional<Mpa> getById(Long id);
}