package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class User {
    @Builder.Default
    private Long id = Long.valueOf(0);;
    @Builder.Default
    private String email = "Default email";
    @Builder.Default
    private String login = "Default login";
    @Builder.Default
    private String name = "Default name";
    @Builder.Default
    private Instant birthday = Instant.parse("2000-12-28T00:00:00.00Z");
}
