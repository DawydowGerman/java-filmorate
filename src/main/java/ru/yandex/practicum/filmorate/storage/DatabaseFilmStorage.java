package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
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
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
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
    public Optional<Film> getFilmById(Long filmId) {
        String sqlQuery = "select film_id, name, description, releasedate, duration, mparating_id " +
                "from films where film_id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, filmRowMapper, filmId);
        if (film != null) {
            return Optional.of(film);
        }
        return Optional.empty();
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

    public List<Film> getMostPopularFilms() {
        String sqlQuery = "SELECT FILM_ID,\n" +
                "       NAME,\n" +
                "       DESCRIPTION,\n" +
                "       RELEASEDATE,\n" +
                "       DURATION,\n" +
                "       MPARATING_ID\n" +
                "FROM (\n" +
                "\n" +
                "select f.FILM_ID,\n" +
                "       f.NAME,\n" +
                "       f.DESCRIPTION,\n" +
                "       f.RELEASEDATE,\n" +
                "       f.DURATION,\n" +
                "       f.MPARATING_ID,\n" +
                "       \n" +
                "       COUNT (f.FILM_ID) as count_value\n" +
                "       \n" +
                "from likes as l\n" +
                "INNER JOIN films as f ON f.film_id = l.film_id\n" +
                "\n" +
                "GROUP BY f.FILM_ID\n" +
                "ORDER BY count_value desc )";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        return result;
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "select сf.*\n" +
                "from  (select f.*\n" +
                "       from likes l1\n" +
                "       inner join films f\n" +
                "           on f.film_id = l1.film_id\n" +
                "       inner join likes l2\n" +
                "           on l2.film_id = l1.film_id\n" +
                "           and l2.user_id = ?\n" +
                "       where l1.user_id = ?) сf\n" +
                "inner join (select l.film_id, count(l.user_id) as cnt\n" +
                "           from likes l\n" +
                "           group by l.film_id) сl\n" +
                "   on сl.film_id = сf.film_id\n" +
                "order by сl.cnt desc";

        return jdbcTemplate.query(sqlQuery, filmRowMapper, userId, friendId);
    }

    @Override
    public Optional<List<Film>> getRecommendations(Long userId) {
        String sqlQuery = "SELECT *\n" +
                "FROM (\n" +
                "\n" +
                "SELECT *\n" +
                "FROM FILMS\n" +
                "WHERE FILM_ID in (\n" +
                "\n" +
                "SELECT FILM_ID\n" +
                "FROM LIKES\n" +
                "WHERE USER_ID in (\n" +
                "\n" +
                "SELECT USER_ID\n" +
                "FROM LIKES\n" +
                "WHERE FILM_ID in (\n" +
                "                  SELECT FILM_ID \n" +
                "                  FROM LIKES\n" +
                "                  WHERE USER_ID = ?\n" +
                "                 ) \n" +
                "              AND NOT USER_ID = ?\n" +
                "                  )\n" +
                ")\n" +
                "\n" +
                ") WHERE NOT FILM_ID in (SELECT FILM_ID\n" +
                "FROM LIKES\n" +
                "WHERE USER_ID = ?\n" +
                ") ";

        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper, userId, userId, userId);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public boolean isFilmIdExists(Long id) {
        String sql = "SELECT count(*) FROM films WHERE film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count > 0;
    }

    public List<Film> getFilmsByDirector(final Long directorId, final String sort) {
        String sqlSortOrder = "ASC";
        String subQuery = "YEAR(f.RELEASEDATE)";

        if (sort.equals("likes")) {
            sqlSortOrder = "DESC";
            subQuery = "(SELECT COUNT(fl.*) FROM likes fl WHERE fl.film_id = f.film_id)";
        }

        return jdbcTemplate.query(
                "SELECT res.* FROM (SELECT f.*, " + subQuery + " _rate "
                        + "FROM films f "
                        + "INNER JOIN film_directors fd ON f.film_id = fd.film_id "
                        + "WHERE fd.director_id = ? ) as res "
                        + "ORDER BY res._rate " + sqlSortOrder,
                new FilmRowMapper(),
                directorId
        );
    }
}
