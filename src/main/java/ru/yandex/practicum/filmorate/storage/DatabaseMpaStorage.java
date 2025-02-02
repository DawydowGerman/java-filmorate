package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository("DatabaseMpaStorage")
@RequiredArgsConstructor
public class DatabaseMpaStorage implements MpaStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public boolean exists(Long id) {
        return getById(id).isPresent();
    }

    @Override
    public List<Mpa> findAll() {
        String query = "select * from mparating";
        return jdbc.query(query, mpaRowMapper);
    }

    @Override
    public Optional<Mpa> getById(Long id) {
        String query = "select * from mparating where mparating_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            Mpa mpa = jdbc.query(query, params, mpaRowMapper).getFirst();
            return Optional.of(mpa);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
