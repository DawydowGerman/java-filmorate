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
        jdbcTemplate.update("DELETE FROM film_directors WHERE film_id = ?", film.getId());

        String sqlQuery = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, film.getId());
                        preparedStatement.setLong(2, film.getDirectors().get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getDirectors().size();
                    }
                }
        );
    }

    public boolean isFilmHasDirector(final Long filmId) {
        return !jdbcTemplate.query(
                "SELECT director_id as id FROM film_directors WHERE film_id = ?",
                new DirectorRowMapper(),
                filmId
        ).isEmpty();
    }

    public List<Director> getDirectorOfFilm(Long filmId) {
        return jdbcTemplate.query(
                "SELECT d.* "
                        + "FROM directors d "
                        + "INNER JOIN film_directors fd ON fd.director_id = d.id "
                        + "WHERE fd.film_id = ?",
                new DirectorRowMapper(),
                filmId
        );
    }
}
