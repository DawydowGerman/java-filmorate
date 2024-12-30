package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFilmStorage")
public class DatabaseFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

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
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Optional<List<Film>> findAll() {
        String sqlQuery = "select film_id, name, description, releasedate, duration, mparating_id " +
                "from films";
         List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, releasedate = ?, duration = ?, mparating_id = ? " +
                "where film_id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.empty();
    }

    public List<Film> getMostPopularFilms() {
        String sqlQuery = "SELECT *\n" +
                "FROM FILMS\n" +
                "WHERE FILM_ID IN\n" +
                "               (SELECT FILM_ID\n" +
                "                             FROM (SELECT FILM_ID,\n" +
                "                                  COUNT (FILM_ID) as count_value\n" +
                "                                  FROM LIKES\n" +
                "                                  GROUP BY FILM_ID\n" +
                "                                  ORDER BY count_value desc))\n" +
                "ORDER BY FILM_ID desc";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        return result;
    }

    public boolean isFilmIdExists(Long id) {
        String sql = "SELECT count(*) FROM films WHERE film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count > 0;
    }
}
