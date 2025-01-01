package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private Long id;
    private String name;

    public Mpa() {
    }

    public Mpa(Long id) {
        this.id = id;
    }

    public Mpa(String id) {
        this.id = Long.parseLong(id);
    }

    public Mpa(Long id, String mpaRating) {
        this.id = id;
        this.name = mpaRating;
    }

    public long getId() {
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