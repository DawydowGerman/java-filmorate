package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFilmGenresStorage")
public class DatabaseFilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public void saveFilmGenres(Film film) {
        if (film == null || film.getGenres() == null || film.getGenres().isEmpty()) throw new IllegalArgumentException("Фильм должен иметь жанры.");
        String sqlQuery = "INSERT INTO film_genres (film_id, genres_id) " +
                "VALUES (?, ?)";
        if (film.getGenres().size() > 1) {
            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setLong(1, film.getId());
                            preparedStatement.setLong(2, film.getGenres().get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return film.getGenres().size();
                        }
                    }
            );
        } else jdbcTemplate.update(sqlQuery, film.getId(), film.getGenres().get(0).getId());
    }

    public void updateFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            saveFilmGenres(film);
        }
    }

    public boolean isFilmHasGenre(Long filmId) {
        if (filmId == null) return false;
        String sql = "SELECT count(*) FROM film_genres WHERE film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{filmId}, Integer.class);
        return count > 0;
    }

    public List<Long> getGenresIdsOfFilm(Long filmId) {
        if (filmId == null) return Collections.emptyList();
        String sqlQuery = "SELECT GENRES_ID \n" +
                "FROM FILM_GENRES\n" +
                "WHERE FILM_ID = ?";
        List<Long> result = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        return result != null ? result : Collections.emptyList();
    }
}