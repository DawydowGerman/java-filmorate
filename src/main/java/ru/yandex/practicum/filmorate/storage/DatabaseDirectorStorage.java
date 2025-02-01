package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DatabaseDirectorStorage implements DirectorStorage {

    private final JdbcTemplate jdbc;
    private final DirectorRowMapper mapper = new DirectorRowMapper();

    @Override
    public List<Director> getAll() {
        return jdbc.query("SELECT * FROM directors", mapper);
    }

    @Override
    public Director findById(final Long id) {
        List<Director> list = jdbc.query("SELECT * FROM directors WHERE id = ?", mapper, id);

        if (list.isEmpty()) {
            return null;
        }

        return list.getFirst();
    }

    @Override
    public Director create(final Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbc.update(
                    conn -> {
                        PreparedStatement stmt = conn.prepareStatement(
                                "INSERT INTO directors (name) VALUES (?)",
                                Statement.RETURN_GENERATED_KEYS
                        );

                        stmt.setString(1, director.getName());

                        return stmt;
                    },
                    keyHolder
            );
        } catch (DataAccessException e) {
            throw new InternalServerException("Error on saving data");
        }

        Long id = keyHolder.getKeyAs(Long.class);

        if (id == null) {
            throw new InternalServerException("Error on saving data");
        }

        director.setId(id);

        return director;
    }

    @Override
    public Director update(final Director director) {
        try {
            jdbc.update(
                    "UPDATE directors SET name = ? WHERE id = ?",
                    director.getName(),
                    director.getId()
            );
        } catch (DataAccessException e) {
            throw new InternalServerException("Error on updating data");
        }

        return director;
    }

    @Override
    public void deleteById(final Long id) {
        jdbc.update("DELETE FROM directors WHERE id = ?", id);
    }

    @Override
    public boolean existsById(final Long id) {
        try {
            return jdbc.query("SELECT id FROM directors WHERE id = ?", mapper, id).getFirst() != null;
        } catch (DataAccessException e) {
            return false;
        }
    }

}
