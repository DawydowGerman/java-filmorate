package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.PreparedStatement;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Component("DatabaseUserStorage")
public class DatabaseUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User create(User user) {
        String sqlQuery =
                "INSERT INTO users (email, login, name, birthday)" + "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[] {"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where user_id = ?";

        jdbcTemplate.update(sqlQuery,
                 user.getEmail(),
                 user.getLogin(),
                 user.getName(),
                 user.getBirthday(),
                 user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        String sqlQuery = "select user_id, email, login, name, birthday " +
                "from users where user_id = ?";
        User user = jdbcTemplate.queryForObject(sqlQuery, userRowMapper, userId);
        if (user != null) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select user_id, email, login, name, birthday from users";
        Collection<User> result = jdbcTemplate.query(sqlQuery, userRowMapper);
        if (result.size() == 0 || result == null) {
            return null;
        }
        return result;
    }

    public boolean isUserIdExists(Long id) {
        String sql = "SELECT count(*) FROM users WHERE user_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[] { id }, Integer.class);
        return count > 0;
    }
}
