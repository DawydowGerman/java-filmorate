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
        String sqlQuery = "insert into likes (user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}