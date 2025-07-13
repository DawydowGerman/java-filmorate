package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Boolean exists(Long id);

    List<Genre> findAll();

    Optional<Genre> getById(Long id);
}
