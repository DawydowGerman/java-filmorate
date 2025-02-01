package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository("DatabaseGenreStorage")
@RequiredArgsConstructor
public class DatabaseGenreStorage implements GenreStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public boolean exists(Long id) {
        return getById(id).isPresent();
    }

    @Override
    public List<Genre> findAll() {
        String query = "select * from genres";
        return jdbc.query(query, genreRowMapper);
    }

    @Override
    public Optional<Genre> getById(Long id) {
        String query = "select * from genres where genres_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            Genre genre = jdbc.query(query, params, genreRowMapper).getFirst();
            return Optional.of(genre);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
