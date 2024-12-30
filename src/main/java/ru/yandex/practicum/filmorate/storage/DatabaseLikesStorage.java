package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@RequiredArgsConstructor
@Repository
@Component("DatabaseLikesStorage")
public class DatabaseLikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public void saveFilmLikes(Long userId, Long filmId) {
        String sqlQuery = "insert into likes (user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}
