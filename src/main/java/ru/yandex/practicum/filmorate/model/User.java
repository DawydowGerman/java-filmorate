package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @Builder.Default
    private Long id = Long.valueOf(0);
    @Builder.Default
    private String email = "Default email";
    @Builder.Default
    private String login = "Default login";
    @Builder.Default
    private String name = "Default name";
    @Builder.Default
    private LocalDate birthday = LocalDate.of(2000, 12, 01);
}
