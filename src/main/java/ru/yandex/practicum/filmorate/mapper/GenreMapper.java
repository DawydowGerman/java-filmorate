package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {
    public static Genre toModel(GenreDTO genreDTO) {
        Genre genre = new Genre(genreDTO.getId(), genreDTO.getName());
        return genre;
    }

    public static GenreDTO toDto(Genre genre) {
        GenreDTO genreDTO = new GenreDTO(genre.getId(), genre.getName());
        return genreDTO;
    }
}