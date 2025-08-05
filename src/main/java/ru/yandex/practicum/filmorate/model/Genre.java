package ru.yandex.practicum.filmorate.model;

import lombok.NonNull;
import lombok.Data;

@Data
public class Genre {
    @NonNull
    private Long id;

    @NonNull
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