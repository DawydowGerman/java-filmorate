package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFilmGenresStorage")
public class DatabaseFilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public void saveFilmGenres(Film film) {
        String sqlQuery = "insert into film_genres (film_id, genres_id) " +
                "values (?, ?)";
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

    public boolean isFilmHasGenre(Long filmId) {
        String sql = "SELECT count(*) FROM film_genres WHERE film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{filmId}, Integer.class);
        return count > 0;
    }

    public List<Long> getGenresIdsOfFilm(Long filmId) {
        String sqlQuery = "SELECT GENRES_ID \n" +
                "FROM FILM_GENRES\n" +
                "where FILM_ID = ?";
        List<Long> resultList = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        return resultList;
    }
}