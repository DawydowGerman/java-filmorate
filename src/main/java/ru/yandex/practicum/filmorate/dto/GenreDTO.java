package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class GenreDTO {
    private Long id;
    private String name;

    public GenreDTO() {
    }

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}