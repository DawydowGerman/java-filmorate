package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private Long id;

    public Mpa() {
    }

    public Mpa(Long id) {
        this.id = id;
    }

    public Mpa(String id) {
        this.id = Long.parseLong(id);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}