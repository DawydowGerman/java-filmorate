package ru.yandex.practicum.filmorate.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorRowMapper;

@RequiredArgsConstructor
@Repository
public class DatabaseFilmDirectorsStorage {
    private final JdbcTemplate jdbcTemplate;

    public void saveFilmDirectors(Film film) {
        String sqlQuery = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, film.getId());
                        preparedStatement.setLong(2, film.getDirectors().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                }
        );
    }

    public boolean isFilmHasDirector(final Long filmId) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM film_directors WHERE film_id = ?",
                Long.class,
                filmId
        ) > 0;
    }

    public List<Director> getDirectorIdsOfFilm(Long filmId) {
        return jdbcTemplate.query(
                "SELECT director_id FROM film_directors WHERE film_id = ?",
                new DirectorRowMapper(),
                filmId
        );
    }
}
