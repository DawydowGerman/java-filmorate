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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String mpaRating) {
        this.name = mpaRating;
    }
}