package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.DirectorDTO;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorMapper {

    public static Director toModel(DirectorDTO directorDTO) {
        return new Director(directorDTO.getId(), directorDTO.getName());
    }

    public static DirectorDTO toDto(Director director) {
        return new DirectorDTO(director.getId(), director.getName());
    }

}
