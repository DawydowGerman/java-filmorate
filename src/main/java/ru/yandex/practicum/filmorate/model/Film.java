package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.Instant;
import lombok.Data;
import lombok.Builder;

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
    private Instant releaseDate = Instant.parse("2000-12-28T00:00:00.00Z");
    @Builder.Default
    private Duration duration = Duration.ofMinutes(110);
}
