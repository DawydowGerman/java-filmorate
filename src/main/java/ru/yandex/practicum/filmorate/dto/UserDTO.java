package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {
    private Long id = Long.valueOf(0);
    private String email = "Default email";
    private String login = "Default login";
    private String name = "Default name";
    private LocalDate birthday = LocalDate.of(2000, 12, 01);
    private Set<Long> friends = new HashSet<>();
}