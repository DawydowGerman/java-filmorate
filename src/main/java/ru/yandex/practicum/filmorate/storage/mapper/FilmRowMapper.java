package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timestamp releaseDate = resultSet.getTimestamp("RELEASEDATE");
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(releaseDate.toLocalDateTime().toLocalDate())
                .duration(resultSet.getLong("DURATION"))
             //   .mpa(new Mpa(resultSet.getLong("MPARATING_ID")))


                .build();



     //   return null;
    }

}
