package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.FilmRequestDTO;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film toModelCreate(FilmRequestDTO filmDTO) {
        return Film.builder()
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .mpa(filmDTO.getMpa())
                .genres(filmDTO.getGenres())
                .directors(filmDTO.getDirectors())
                .build();
    }

    public static Film toModelUpdate(FilmDTO filmDTO) {
        return Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(filmDTO.getReleaseDate())
                .duration(filmDTO.getDuration())
                .mpa(filmDTO.getMpa())
                .genres(filmDTO.getGenres())
                .directors(filmDTO.getDirectors())
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
        filmDTO.setDirectors(film.getDirectors());

        return filmDTO;
    }
}
