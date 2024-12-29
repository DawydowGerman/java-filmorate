package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFriendshipStorage")
public class DatabaseFriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public void addFriend(Long id, Long friendId) {
        String sqlQuery0 = "INSERT INTO friendship (user_id, friend_id)" + "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery0,
                id,
                friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE USER_ID = " + id
                + " AND FRIEND_ID = " + friendId;
        jdbcTemplate.update(sqlQuery);
    }

    public Optional<List<User>> getFriends(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN " +
                "(select friend_id from friendship where user_id = " + id + ")";
        List<User> result = jdbcTemplate.query(sqlQuery, userRowMapper);
        if (result != null && result.size() > 0) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public Optional<List<User>> getMutualFriends(Long idUser0, Long idUser1) {
        String sqlQuery = "select * " +
                "from users " +
                "where user_id IN (SELECT friend_id " +
                                  "FROM (SELECT friend_id " +
                                        "FROM friendship " +
                                        "WHERE user_id = " + idUser0 + " OR user_id = " + idUser1 + ")" +
                                  "GROUP BY friend_id " +
                                  "HAVING COUNT(friend_id) > 1)";
        List<User> result = jdbcTemplate.query(sqlQuery, userRowMapper);
        if (result != null && result.size() > 0) {
            return Optional.of(result);
        }
        return Optional.empty();
    }
}