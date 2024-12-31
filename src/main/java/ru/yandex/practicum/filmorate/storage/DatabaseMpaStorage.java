package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Component("DatabaseMpaStorage")
public class DatabaseMpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    public Optional<Mpa> getMpaById(Long mpaId) {
        String sqlQuery = "select mparating_id, name " +
                "from mparating where mparating_id = ?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, mpaRowMapper, mpaId);
            if (mpa != null) {
                return Optional.of(mpa);
            }
            return Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Mpa с " + mpaId + " отсутствует.");
        }
    }

    public Optional<List<Mpa>> findAll() {
        String sqlQuery = "select mparating_id, name from mparating";
        List<Mpa> result = jdbcTemplate.query(sqlQuery, mpaRowMapper);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public boolean isMpaIdExists(Long id) {
        String sqlQuery = "SELECT count(*) FROM mparating WHERE mparating_id = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, new Object[] { id }, Integer.class);
        return count > 0;
    }
}