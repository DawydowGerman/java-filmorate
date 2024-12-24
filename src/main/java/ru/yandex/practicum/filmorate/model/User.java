package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

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
    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public void setToFriends(Long id) {
        friends.add(id);
    }

    public void removeFromFriends(Long id) {
        friends.remove(id);
    }

    public boolean isFriend(Long id) {
        return friends.contains(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", friends=" + friends +
                '}';
    }
}