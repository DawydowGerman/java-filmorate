package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Director {
    private Long id;
    @NotNull
    @NotBlank
    private String name;

    public Director(Long id) {
        this.id = id;
    }

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
