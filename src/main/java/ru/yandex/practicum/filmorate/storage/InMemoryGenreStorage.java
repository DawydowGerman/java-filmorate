package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryGenreStorage implements GenreStorage {

    private static final Map<Long, Genre> GENRE_MAP = Map.ofEntries(
            Map.entry(1L, new Genre(1L, "Комедия")),
            Map.entry(2L, new Genre(1L, "Драма")),
            Map.entry(3L, new Genre(1L, "Мультфильм")),
            Map.entry(4L, new Genre(1L, "Триллер")),
            Map.entry(5L, new Genre(1L, "Документальный")),
            Map.entry(6L, new Genre(1L, "Боевик"))
    );

    @Override
    public boolean exists(Long id) {
        return GENRE_MAP.containsKey(id);
    }

    @Override
    public List<Genre> findAll() {
        return GENRE_MAP.values().stream().toList();
    }

    @Override
    public Optional<Genre> getById(Long id) {
        if (GENRE_MAP.containsKey(id)) {
            return Optional.of(GENRE_MAP.get(id));
        }
        return Optional.empty();
    }
}
