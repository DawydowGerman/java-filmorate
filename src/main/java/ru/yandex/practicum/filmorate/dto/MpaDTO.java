package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class MpaDTO {
    private Long id;
    private String name;

    public MpaDTO() {
    }

    public MpaDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}