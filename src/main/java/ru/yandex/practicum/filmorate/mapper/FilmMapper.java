package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {
    public static Film toModel(FilmDTO filmDTO) {
        return Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .mpa(filmDTO.getMpa())
                .genres(filmDTO.getGenres())
                .build();
    }

    public static FilmDTO toDto(Film film) {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(film.getId());
        filmDTO.setName(film.getName());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseDate(film.getReleaseDate());
        filmDTO.setDuration(film.getDuration());
        filmDTO.setMpa(film.getMpa());
        filmDTO.setGenres(film.getGenres());
        return filmDTO;
    }
}