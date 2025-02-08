package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryMpaStorage implements MpaStorage {

    private static final Map<Long, Mpa> MPA_MAP = Map.ofEntries(
            Map.entry(1L, new Mpa(1L, "G")),
            Map.entry(2L, new Mpa(1L, "PG")),
            Map.entry(3L, new Mpa(1L, "PG-13")),
            Map.entry(4L, new Mpa(1L, "R")),
            Map.entry(5L, new Mpa(1L, "NC-17"))
    );

    @Override
    public Boolean exists(Long id) {
        return MPA_MAP.containsKey(id);
    }

    @Override
    public List<Mpa> findAll() {
        return MPA_MAP.values().stream().toList();
    }

    @Override
    public Optional<Mpa> getById(Long id) {
        if (MPA_MAP.containsKey(id)) {
            return Optional.of(MPA_MAP.get(id));
        }
        return Optional.empty();
    }
}
