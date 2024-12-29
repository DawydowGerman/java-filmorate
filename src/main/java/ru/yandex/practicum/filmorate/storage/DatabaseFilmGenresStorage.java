package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@RequiredArgsConstructor
@Repository
@Component("DatabaseFilmGenresStorage")
public class DatabaseFilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public void saveFilmGenres(Film film) {
        String sqlQuery = "insert into film_genres (film_id, genres_id) " +
                "values (?, ?)";
        if (film.getGenres().size() > 1) {
            for (int i = 0; i < film.getGenres().size(); i++) {
                jdbcTemplate.update(sqlQuery, film.getId(), film.getGenres().get(i).getId());
            }
        } else jdbcTemplate.update(sqlQuery, film.getId(), film.getGenres().get(0).getId());
    }
}