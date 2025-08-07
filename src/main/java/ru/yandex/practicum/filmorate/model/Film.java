package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film implements Comparable<Film> {
    @Builder.Default
    private Long id = Long.valueOf(0);

    @NotNull
    @Builder.Default
    private String name = "";

    @Builder.Default
    private String description = "";

    @NotNull
    @Builder.Default
    private LocalDate releaseDate = LocalDate.of(2000, 12, 28);

    @NotNull
    @Builder.Default
    private Long duration = Long.valueOf(110);

    @NotNull
    @Builder.Default
    private Mpa mpa = new Mpa(1L);

    @Builder.Default
    private List<Genre> genres = new ArrayList<>();

    @Builder.Default
    private List<Director> directors = new ArrayList<>();

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
        return Integer.compare(obj.getLikes().size(), this.getLikes().size());
    }
}