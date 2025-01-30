package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    public List<Director> getAll();

    public Director findById(Long id);

    public Director create(Director director);

    public Director update(Director director);

    public void deleteById(Long id);

    public boolean existsById(Long id);
}
