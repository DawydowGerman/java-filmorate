package ru.yandex.practicum.filmorate.model;

import lombok.NonNull;
import lombok.Data;

@Data
public class Mpa {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    public Mpa() {
    }

    public Mpa(Long id) {
        this.id = id;
    }

    public Mpa(String id) {
        this.id = Long.parseLong(id);
    }

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}