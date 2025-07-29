package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Data
@Builder
public class User {
    @Builder.Default
    private Long id = 0L;

    @Builder.Default
    @NonNull
    private String email = "";

    @Builder.Default
    @NonNull
    private String login = "";

    @Builder.Default
    @NonNull
    private String name = "";

    @Builder.Default
    private LocalDate birthday = LocalDate.of(2000, 12, 1);

    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public void setToFriends(Long id) {
        Objects.requireNonNull(id, "Friend ID cannot be null");
        if (id.equals(this.id)) throw new ValidationException("Cannot add self as friend");
        friends.add(id);
    }

    public boolean removeFromFriends(Long id) {
        Objects.requireNonNull(id, "Friend ID cannot be null");
        return friends.remove(id);
    }

    public boolean isFriend(Long id) {
        Objects.requireNonNull(id, "Friend ID cannot be null");
        return friends.contains(id);
    }
}