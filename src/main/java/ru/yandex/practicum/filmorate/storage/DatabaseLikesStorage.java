package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Component("DatabaseLikesStorage")
public class DatabaseLikesStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public void giveLike(Long userId, Long filmId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        String sqlQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public boolean isFilmHasLike(Long userId, Long filmId) {
        String sql = "SELECT count(*) FROM likes WHERE user_id = ? AND film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, userId, filmId);
        return count > 0;
    }
}