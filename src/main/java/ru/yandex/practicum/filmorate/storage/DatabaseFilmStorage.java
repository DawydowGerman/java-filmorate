package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFilmStorage")
public class DatabaseFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sqlQuery =
               "INSERT INTO films (name, description, releasedate, duration, mparating_id)"
               + "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] {"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setLong(4, film.getDuration());
        //    stmt.setObject(5, film.getMpa());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return List.of();
    }

    @Override
    public Film update(Film newFilm) {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.empty();
    }
}
