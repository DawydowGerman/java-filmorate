package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
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
        String sqlQuery = "SELECT film_id, name, description, releasedate, duration, mparating_id " +
                "FROM films WHERE film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, filmRowMapper, filmId);
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Film>> findAll() {
        String sqlQuery = "SELECT film_id, name, description, releasedate, duration, mparating_id " +
                "FROM films";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<List<Film>> getMostPopularByDirectorOrTitle(String query) {
        String sqlQuery = "SELECT FILM_ID,\n" +
                "       NAME,\n" +
                "       DESCRIPTION,\n" +
                "       RELEASEDATE,\n" +
                "       DURATION,\n" +
                "       MPARATING_ID\n" +
                "FROM (\n" +
                "SELECT f.*, COUNT(l.film_id) like_cnt\n" +
                "FROM films AS f\n" +
                "LEFT JOIN likes as l ON (l.film_id = f.film_id) \n" +
                "GROUP BY f.film_id\n" +
                "ORDER BY like_cnt DESC )\n" +
                "WHERE FILM_ID IN\n" +
                "(SELECT FILM_ID\n" +
                "FROM FILM_DIRECTORS\n" +
                "WHERE DIRECTOR_ID IN (\n" +
                "                      SELECT ID\n" +
                "                      from DIRECTORS\n" +
                "                      WHERE UPPER(NAME) LIKE UPPER('%" + query + "%')))" +
                "OR UPPER(NAME) LIKE UPPER('%" + query + "%')";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Film>> findByDirectorName(String query) {
        String sqlQuery = "SELECT FILM_ID,\n" +
                "       NAME,\n" +
                "       DESCRIPTION,\n" +
                "       RELEASEDATE,\n" +
                "       DURATION,\n" +
                "       MPARATING_ID\n" +
                "FROM (\n" +
                "SELECT f.*, COUNT(l.film_id) like_cnt\n" +
                "FROM films AS f\n" +
                "LEFT JOIN likes as l ON (l.film_id = f.film_id) \n" +
                "GROUP BY f.film_id\n" +
                "ORDER BY like_cnt DESC )\n" +
                "WHERE FILM_ID IN\n" +
                "(SELECT FILM_ID\n" +
                "FROM FILM_DIRECTORS\n" +
                "WHERE DIRECTOR_ID IN (\n" +
                "                      SELECT ID\n" +
                "                      from DIRECTORS\n" +
                "                      WHERE UPPER(NAME) LIKE UPPER('%" + query + "%')))";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Film>> findByFilmTitle(String query) {
        String sqlQuery = "SELECT FILM_ID,\n" +
                "       NAME,\n" +
                "       DESCRIPTION,\n" +
                "       RELEASEDATE,\n" +
                "       DURATION,\n" +
                "       MPARATING_ID\n" +
                "FROM (\n" +
                "SELECT f.*, COUNT(l.film_id) like_cnt\n" +
                "FROM films AS f\n" +
                "LEFT JOIN likes as l ON (l.film_id = f.film_id) \n" +
                "GROUP BY f.film_id\n" +
                "ORDER BY like_cnt DESC )\n" +
                "WHERE UPPER(NAME) LIKE UPPER('%" + query + "%')";
        List<Film> result = jdbcTemplate.query(sqlQuery, filmRowMapper);
        if (result.size() != 0 || result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films set " +
                "name = ?, description = ?, releasedate = ?, duration = ?, mparating_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void remove(Long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public List<Film> getMostPopularFilms(Integer limit, Integer genreId, Integer year) {
        String sqlQuery = "SELECT f.*, COUNT(l.film_id) like_cnt " +
                "FROM films AS f " +
                "LEFT JOIN likes as l ON (l.film_id = f.film_id) ";

        List<String> whereClause = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (genreId != null && genreId > 0) {
            sqlQuery += "INNER JOIN film_genres fg ON fg.film_id = f.film_id ";
            whereClause.add("fg.genres_id = ? ");
            params.add(genreId);
        }

        if (year != null && year > 0) {
            whereClause.add("YEAR(f.releasedate) = ? ");
            params.add(year);
        }

        params.add(limit);

        if (!whereClause.isEmpty()) {
            sqlQuery += "WHERE " + String.join(" AND ", whereClause) + " ";
        }

        sqlQuery += "GROUP BY f.film_id " +
                "ORDER BY like_cnt DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, filmRowMapper, params.toArray());
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "SELECT сf.*\n" +
                "FROM  (SELECT f.*\n" +
                "       FROM likes l1\n" +
                "       INNER JOIN films f\n" +
                "           ON f.film_id = l1.film_id\n" +
                "       INNER JOIN likes l2\n" +
                "           ON l2.film_id = l1.film_id\n" +
                "           AND l2.user_id = ?\n" +
                "       WHERE l1.user_id = ?) сf\n" +
                "INNER JOIN (SELECT l.film_id, count(l.user_id) AS cnt\n" +
                "           FROM likes l\n" +
                "           GROUP BY l.film_id) сl\n" +
                "   ON сl.film_id = сf.film_id\n" +
                "ORDER BY сl.cnt desc";

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

    public Boolean isFilmIdExists(Long id) {
        String sql = "SELECT count(*) FROM films WHERE film_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count > 0;
    }

    public Boolean isFilmTitleExists(String name) {
        name = "%" + name + "%";
        String sql = "SELECT count(*) FROM films WHERE NAME LIKE ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{name}, Integer.class);
        return count > 0;
    }

    public Optional<List<Film>> getFilmsByDirector(final Long directorId, final String sort) {
        String sqlSortOrder = "ASC";
        String subQuery = "YEAR(f.RELEASEDATE)";

        if (sort.equals("likes")) {
            sqlSortOrder = "DESC";
            subQuery = "(SELECT COUNT(fl.*) FROM likes fl WHERE fl.film_id = f.film_id)";
        }

        List<Film> result =  jdbcTemplate.query(
                "SELECT res.* FROM (SELECT f.*, " + subQuery + " _rate "
                        + "FROM films f "
                        + "INNER JOIN film_directors fd ON f.film_id = fd.film_id "
                        + "WHERE fd.director_id = ? ) as res "
                        + "ORDER BY res._rate " + sqlSortOrder,
                new FilmRowMapper(),
                directorId
        );
        if (result.size() != 0) {
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
