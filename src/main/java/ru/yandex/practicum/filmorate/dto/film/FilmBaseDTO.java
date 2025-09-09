package ru.yandex.practicum.filmorate.dto.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

public interface FilmBaseDTO {
    String getName();
    String getDescription();
    LocalDate getReleaseDate();
    Long getDuration();
    Mpa getMpa();
    List<Genre> getGenres();
    List<Director> getDirectors();
    void setGenres(List<Genre> genres);
    void setDirectors(List<Director> directors);
}