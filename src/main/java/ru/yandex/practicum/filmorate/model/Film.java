package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @Builder.Default
    private Long id = Long.valueOf(0);
    @Builder.Default
    private String name = "Default name";
    @Builder.Default
    private String description = "Default description";
    @Builder.Default
    private LocalDate releaseDate = LocalDate.of(2000, 12,28);
    @Builder.Default
    private Integer duration = Integer.valueOf(110);
}
