package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectorDTO {
    private Long id;
    private String name;

    public DirectorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
