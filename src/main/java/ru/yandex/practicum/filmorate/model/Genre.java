package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private Long id;
    private String name;

    public Genre() {
    }

    public Genre(Long id) {
        this.id = id;
    }

    public Genre(String id) {
        this.id = Long.parseLong(id);
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}