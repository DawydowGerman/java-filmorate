package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void giveLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);

    boolean isFilmHasLike(Long userId, Long filmId);
}