package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.valueOf;

@Data
public class FilmDTO {
    private Long id = valueOf(0);
    private String name = "Default name";
    private String description = "Default description";
    private LocalDate releaseDate = LocalDate.of(2000, 12,28);
    private Long duration = valueOf(110);
    private Mpa mpa = new Mpa(valueOf(0));
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
