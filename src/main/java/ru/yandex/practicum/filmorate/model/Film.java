package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.*;

@Data
@Builder
public class Film implements Comparable<Film> {
    @Builder.Default
    private Long id = Long.valueOf(0);
    @Builder.Default
    private String name = "Default name";
    @Builder.Default
    private String description = "Default description";
    @NotNull
    @Builder.Default
    private LocalDate releaseDate = LocalDate.of(2000, 12,28);
    @Min(1)
    @Builder.Default
    private Integer duration = Integer.valueOf(110);
    @Builder.Default
    private Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

    public int compareTo(Film obj) {
        return obj.getLikes().size() - this.getLikes().size();
    }
}
