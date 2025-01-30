package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Director {
    private Long id;
    private String name;

    public Director(Long id) {
        this.id = id;
    }

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
