package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilmDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();

    public FilmDTO() {
    }

    public FilmDTO(Long id, String name, String description,
                   LocalDate releaseDate, Long duration, Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }
}